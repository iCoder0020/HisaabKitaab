import json
import requests
payload = {"username":"iGamer", "password":"ishansang"}
headers = {'Content-type': 'application/json'}
r = requests.get("http://127.0.0.1:8000/login/", data=json.dumps(payload), headers=headers)
print(r.text)

