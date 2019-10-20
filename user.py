"""
user.py
Abhi Sinha

User model.
"""
from __future__ import annotations


class User:
    def __init__(self, name, number):
        self._location = ""
        self._name = name
        self._number = number
        self._partner = None

    def name(self):
        return self._name

    def number(self):
        return self._number

    def partner(self):
        return self._partner

    def location(self):
        return self._location

    def set_location(self, x, y):
        self._location = str(x) + "," + str(y)
        print(self._location)

    def set_partner(self, partner: User):
        self._partner = partner

    def __str__(self):
        _s = "User(name: " + self._name + ", number: " + self._number
        if self._partner is not None:
            _s += ", partner: " + self._partner.__str__()
        if self._location is not None:
            _s += ", location: " + self._location.__str__()
        _s += ")"
        return _s


if __name__ == "__main__":
    j = User("Jar8", "+19739780831")
    a = User("Abhi", "+19739002003")
    j.set_partner(a)
    j.set_location(40.5008, -74.4474)

