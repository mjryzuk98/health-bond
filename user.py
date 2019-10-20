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
        self._number = "+1" + str(number)
        self._partners = None

    def name(self):
        return self._name

    def number(self):
        return self._number

    def partners(self):
        return self._partners

    def location(self):
        return self._location

    def set_location(self, x, y):
        self._location = str(x) + "," + str(y)
        print(self._location)

    def set_partners(self, partners):
        self._partners = list(map(lambda x: "+1" + str(x), partners))
        print(self._partners)

    def __str__(self):
        _s = "User(name: " + self._name + ", number: " + self._number
        if self._partners is not []:
            _s += ", partners: " + str(self._partners)
        if self._location is not None:
            _s += ", location: " + self._location.__str__()
        _s += ")"
        return _s


if __name__ == "__main__":
    j = User("Jar8", "+19739780831")
    a = User("Abhi", "+19739002003")
    j.set_partners(a)
    j.set_location(40.5008, -74.4474)
