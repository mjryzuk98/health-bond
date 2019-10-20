import requests, json
from config import cfg2

user_name="abhinav"

RADIUS="300"
TYPE="restaurant"

URL_BASE = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?' + "key=" + cfg2["api_key"] + "&radius=" + RADIUS + "&type=" + TYPE

location="40.5008,-74.4474"
url= URL_BASE + "&location=" + location

r = requests.get (url)

response = r.json()



print(user_name + " just passed by " + response["results"][0]["name"] )


from config import cfg
from twilio.rest import Client

client = Client(cfg["account_sid"], cfg["auth_token"])
client_num = "+17732957498"

message = client.messages.create(
    to=+19739002003,
    from_=client_num,
    body= user_name + " just passed by " + response["results"][0]["name"])

print(message.sid)
