"""
user.py
Abhi Sinha

User model.
"""
from __future__ import annotations


class User:
    def __init__(self, name, number):
        self._name = name
        self._number = number
        self._partner = None

    def name(self):
        return self._name

    def number(self):
        return self._number

    def partner(self):
        return self._partner

    def set_partner(self, partner: User):
        self._partner = partner

    def __str__(self):
        _s = "User(name: " + self._name + ", number: " + self._number
        if self._partner is not None:
            _s += ", partner: " + self._partner.__str__()
        _s += ")"
        return _s

