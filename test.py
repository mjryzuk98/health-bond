import json
import requests

from twilio.rest import Client

from config import cfg


RADIUS = "300"
TYPE = "restaurant"
URL_BASE = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?' + \
    "key=" + cfg["api_key"] + "&radius=" + RADIUS + "&type=" + TYPE
CLIENT = Client(cfg["account_sid"], cfg["auth_token"])
CLIENT_NUM = "+17732957498"


def notify(user, number, location):
    url = URL_BASE + "&location=" + location
    response = requests.get(url).json()

    print(user + " just passed by " + response["results"][1]["name"])

    message = CLIENT.messages.create(
        body = user + " just passed by " + response["results"][1]["name"],
        from_ = CLIENT_NUM,
        to = number
    )

    print(message.sid)


if __name__ == "__main__":
    user = "abhinav"
    number = "+19739002003"
    location = "40.5008,-74.4474"
    notify(user, number, location)

