from config import cfg
from twilio.rest import Client

client = Client(cfg["account_sid"], cfg["auth_token"])
client_num = "+17732957498"

message = client.messages.create(
    to="+19737233461",
    from_=client_num,
    body="aoeuaoeuaoeuaoeuhthnthanoeuaoe")

print(message.sid)
