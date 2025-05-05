MESSAGE = 1
LOGOUT = 2
WHOISIN = 3

class ChatMessage:
    def __init__(self, msg_type, message=""):
        self.type = msg_type
        self.message = message
