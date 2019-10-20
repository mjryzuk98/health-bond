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


def notify(user: User):
    url = URL_BASE + "&location=" + user.location()
    res = requests.get(url)
    if res.status_code != 200:
        print("Invalid status code", res.status_code)
        return
    dat = res.json()["results"]
    if not dat:
        print("No workable data recieved...")
        return
    _body = user.name() + " just passed by " + dat[0]["name"]

    msgs = list()

    for partner in user.partners():
        print("Notifying: " + partner)
        messages += CLIENT.messages.create(
            body=_body,
            from_=CLIENT_NUM,
            to=partner
        )

    print(messages.sid)


if __name__ == "__main__":

    j = User("Jar8", "+19739780831")
    a = User("Abhi", "+19739002003")
    j.set_partner(a)
    j.set_location(40.5008, -74.4474)
    notify(j)
