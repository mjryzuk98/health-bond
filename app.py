from flask import Flask
from user import User
import services

app = Flask(__name__)

users = {}  # username: User object


@app.route("/")
def index():
    return "Server is up"

# currently params should be <user_id>:[,params]
# change to better way of passing params


@app.route("/register_user/<account>")  # usrname;name;number
def register_user(account):
    params = account.split(';')
    username = params[0]
    users[username] = User(params[1], params[2])
    print(users[username])
    return "register_user"


# usrname;phone_num;phone_num;...
@app.route("/update_subscribers/<subscribers>")
def update_subscribers(subscribers):
    params = subscribers.split(';')
    username = params[0]
    users[username].set_partners(params[1:])
    print(users[username])
    return "update_subscribers"


@app.route("/coords/<latlong>")  # usrname;latlong
def get_places(latlong):
    params = latlong.split(';')
    username = params[0]
    users[username].set_location(params[1], params[2])
    services.notify(users[username])
    print(users[username])
    return "get_places"


if __name__ == "__main__":
    app.run(debug=False)

