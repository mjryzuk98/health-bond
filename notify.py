import json
import requests

from twilio.rest import Client

from config import cfg
from user import User


CLIENT = Client(cfg["account_sid"], cfg["auth_token"])
CLIENT_NUM = "+17732957498"
RADIUS = cfg["radius"]
TYPE = cfg["type"]

URL_BASE = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?' + \
    "key=" + cfg["api_key"] + "&radius=" + RADIUS + "&type=" + TYPE


def notify(user: User, location):
    url = URL_BASE + "&location=" + location
    res = requests.get(url).json()
    _body = user.name() + " just passed by " + res["results"][0]["name"]
    message = CLIENT.messages.create(
        body = _body,
        from_ = CLIENT_NUM,
        to = user.partner().number()
    )
    print(message.sid)


if __name__ == "__main__":
    j = User("Jar8", "+19739780831")
    a = User("Abhi", "+19739002003")
    j.set_partner(a)
    location = "40.5008,-74.4474"
    notify(j, location)

