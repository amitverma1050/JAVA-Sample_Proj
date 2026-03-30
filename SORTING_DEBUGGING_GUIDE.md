# 🔧 Debugging Sorted Results - Test Guide

## Issue: Results Not Returning in Order

If you're seeing reviews but they're not sorted, here's how to debug and fix it:

---

## Step 1: Enable SQL Query Logging ✅ (DONE)

Your `application.properties` now has enhanced logging:

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

**This will show you the exact SQL queries being generated.**

Look in your console for messages like:
```sql
SELECT review0_.id as id1_1_, review0_.company_id as company_4_1_, ...
FROM review review0_ 
WHERE review0_.company_id = ? 
ORDER BY review0_.rating DESC
```

---

## Step 2: Test with Sample Data

### Create Test Data via API

```bash
# Create a company first
curl -X POST http://localhost:8080/companies \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Company",
    "email": "test@company.com",
    "phone": "1234567890",
    "address": "123 Main St"
  }'

# Note the company ID (let's assume it's 1)

# Add reviews with different ratings
curl -X POST http://localhost:8080/companies/1/reviews \
  -H "Content-Type: application/json" \
  -d '{"title": "Excellent", "description": "Best place", "rating": 5.0}'

curl -X POST http://localhost:8080/companies/1/reviews \
  -H "Content-Type: application/json" \
  -d '{"title": "Average", "description": "Okay place", "rating": 3.0}'

curl -X POST http://localhost:8080/companies/1/reviews \
  -H "Content-Type: application/json" \
  -d '{"title": "Bad", "description": "Not good", "rating": 1.0}'

curl -X POST http://localhost:8080/companies/1/reviews \
  -H "Content-Type: application/json" \
  -d '{"title": "Great", "description": "Very good", "rating": 4.5}'

curl -X POST http://localhost:8080/companies/1/reviews \
  -H "Content-Type: application/json" \
  -d '{"title": "Good", "description": "Pretty good", "rating": 3.5}'
```

---

## Step 3: Test Sorting Endpoints

### Test 1: Unsorted Reviews
```bash
curl http://localhost:8080/companies/1/reviews
```

**Expected:** Reviews in any order (whatever order they're in database)

**Response Example:**
```json
[
  {"id": 1, "title": "Excellent", "rating": 5.0},
  {"id": 2, "title": "Average", "rating": 3.0},
  {"id": 3, "title": "Bad", "rating": 1.0},
  {"id": 4, "title": "Great", "rating": 4.5},
  {"id": 5, "title": "Good", "rating": 3.5}
]
```

### Test 2: Best Reviews First (Highest Ratings)
```bash
curl http://localhost:8080/companies/1/reviews/sorted-by-rating-desc
```

**Expected:** Reviews sorted from 5⭐ down to 1⭐

**Response (CORRECT ORDER):**
```json
[
  {"id": 1, "title": "Excellent", "rating": 5.0},     ← 5.0
  {"id": 4, "title": "Great", "rating": 4.5},         ← 4.5
  {"id": 5, "title": "Good", "rating": 3.5},          ← 3.5
  {"id": 2, "title": "Average", "rating": 3.0},       ← 3.0
  {"id": 3, "title": "Bad", "rating": 1.0}            ← 1.0
]
```

### Test 3: Worst Reviews First (Lowest Ratings)
```bash
curl http://localhost:8080/companies/1/reviews/sorted-by-rating
```

**Expected:** Reviews sorted from 1⭐ up to 5⭐

**Response (CORRECT ORDER):**
```json
[
  {"id": 3, "title": "Bad", "rating": 1.0},           ← 1.0
  {"id": 2, "title": "Average", "rating": 3.0},       ← 3.0
  {"id": 5, "title": "Good", "rating": 3.5},          ← 3.5
  {"id": 4, "title": "Great", "rating": 4.5},         ← 4.5
  {"id": 1, "title": "Excellent", "rating": 5.0}      ← 5.0
]
```

### Test 4: Alphabetically by Title
```bash
curl http://localhost:8080/companies/1/reviews/sorted-by-title
```

**Expected:** Reviews sorted A-Z by title

**Response (CORRECT ORDER):**
```json
[
  {"id": 2, "title": "Average", "rating": 3.0},       ← A
  {"id": 3, "title": "Bad", "rating": 1.0},           ← B
  {"id": 1, "title": "Excellent", "rating": 5.0},     ← E
  {"id": 4, "title": "Great", "rating": 4.5},         ← G
  {"id": 5, "title": "Good", "rating": 3.5}           ← G
]
```

---

## Step 4: Check SQL Console Output

When you make requests, you should see SQL in your console like:

### For `sorted-by-rating-desc`
```sql
select review0_.id as id1_1_, review0_.rating as rating2_1_, review0_.title as title3_1_
from review review0_
where review0_.company_id = 1
order by review0_.rating DESC
```

### For `sorted-by-rating`
```sql
select review0_.id as id1_1_, review0_.rating as rating2_1_, review0_.title as title3_1_
from review review0_
where review0_.company_id = 1
order by review0_.rating ASC
```

### For `sorted-by-title`
```sql
select review0_.id as id1_1_, review0_.rating as rating2_1_, review0_.title as title3_1_
from review review0_
where review0_.company_id = 1
order by review0_.title ASC
```

**If you see these SQL queries, the sorting IS happening at the database level!** ✅

---

## Troubleshooting

### ❌ Problem 1: Rating is null
**Symptom:** All reviews have `"rating": 0.0`

**Solution:** Make sure you're including the `rating` field when creating reviews:
```json
{
  "title": "Title",
  "description": "Description",
  "rating": 5.0    ← REQUIRED
}
```

### ❌ Problem 2: Results still not sorted
**Solution:** Check the console logs:
1. Look for the SQL queries
2. Verify they include `ORDER BY rating DESC` or `ORDER BY rating ASC`
3. If SQL looks correct but results are wrong, restart the application

### ❌ Problem 3: Empty results
**Solution:** 
1. Make sure the company exists: `GET /companies/1`
2. Make sure reviews exist for that company: `GET /companies/1/reviews`
3. Try with an actual company ID you've created

---

## Complete Test Workflow

```bash
# 1. Create company
COMPANY_ID=1

# 2. Add 5 reviews with different ratings
for rating in 5.0 3.0 1.0 4.5 3.5; do
  curl -X POST http://localhost:8080/companies/$COMPANY_ID/reviews \
    -H "Content-Type: application/json" \
    -d "{\"title\": \"Review\", \"description\": \"Test\", \"rating\": $rating}"
done

# 3. Test unsorted
echo "=== UNSORTED ==="
curl http://localhost:8080/companies/$COMPANY_ID/reviews

# 4. Test best first
echo "=== BEST FIRST (DESC) ==="
curl http://localhost:8080/companies/$COMPANY_ID/reviews/sorted-by-rating-desc

# 5. Test worst first
echo "=== WORST FIRST (ASC) ==="
curl http://localhost:8080/companies/$COMPANY_ID/reviews/sorted-by-rating

# 6. Test alphabetical
echo "=== ALPHABETICAL ==="
curl http://localhost:8080/companies/$COMPANY_ID/reviews/sorted-by-title
```

---

## What Changed (Fixes Applied)

✅ **Company.java** - Added cascade settings to reviews relationship  
✅ **Review.java** - Added @Column annotations to ensure proper mapping  
✅ **application.properties** - Enabled SQL query logging  
✅ **ReviewRepository** - Methods already correct  
✅ **ReviewService** - Methods already implemented  
✅ **ReviewController** - Endpoints already exposed  

---

## Expected Console Output

When you request sorted reviews, you should see in console:

```
Hibernate: select review0_.id as id1_1_, review0_.company_id as company_4_1_, 
review0_.description as descript2_1_, review0_.rating as rating3_1_, review0_.title as title5_1_ 
from review review0_ 
where review0_.company_id = ? 
order by review0_.rating DESC
```

This proves the sorting is happening at the DATABASE level! ✅

---

## If It Still Doesn't Work

1. **Restart your application** - Changes to annotations require restart
2. **Clear H2 database** - Delete all data: `DELETE FROM review;`
3. **Check the SQL queries** in console - they should show `ORDER BY`
4. **Verify rating values** - Make sure reviews have non-zero ratings
5. **Check company exists** - The companyId in URL must be valid

---

**Try the tests above and let me know if sorting works!** 🚀
