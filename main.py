from flask import Flask, request, Response

from user import User
import services

app = Flask(__name__)

users = {}

@app.route("/")
def index():
    return "Welcome to Health Bond!"


@app.route("/register/<user>/<name>/<number>")
def register(user=None, name=None, number=None):
    if user is None or name is None or number is None:
        return "Unable to register user"
    if user in users:
        return "User already exists"
    users[user] = User(name, number)
    return Response("Registered " + user + " with name " + name \
        + " and number " + number, mimetype='text/plain')


# subscribers: comma separated list
@app.route("/<user>/update/<subs>")
def update_subscribers(user=None, subs=None):
    if user is None or subs is None:
        return "Unable to update user subscribers"
    if user not in users:
        return "User not found, please register first"
    users[user].set_partners(subs.split(","))
    return Response("Updated subscribers for " + user, mimetype='text/plain')


# xy coords separated by comma
@app.route("/<user>/coords/<loc>")
def get_places(user=None, loc=None):
    if user is None or loc is None:
        return "Unable to update location"
    if user not in users:
        return "User not found, please register first"
    _usr = users[user]
    if _usr.location() == loc:
        services.notify(_usr)
        return "Notified subscribers"
    else:
        coords = loc.split(",")
        if len(coords) < 2:
            return "Invalid location" 
        _usr.set_location(coords[0], coords[1])
        return "Updated location"


if __name__ == "__main__":
    app.run(debug=False)

