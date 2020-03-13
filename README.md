For {"detail": "CSRF Failed: CSRF token missing or incorrect."} error.
Fix:
Put the CSRF Token in Authentication Header.
Key: X-CSRFToken
Value: (get it from cache)
