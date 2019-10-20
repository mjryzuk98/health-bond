"""
user.py
Abhi Sinha

User model.
"""
from __future__ import annotations


class User:
    def __init__(self, name, number):
        self.name = name
        self.number = number
        self.partner = None

    def __str__(self):
        _str = "User(name: " + self.name + ", number: " + self.number
        if self.partner is not None:
            _str += ", partner: " + self.partner.__str__()
        _str += ")"
        return _str

    def name(self):
        return self.name

    def number(self):
        return self.number

    def partner(self):
        return self.partner

    def set_partner(self, partner: User):
        self.partner = partner

