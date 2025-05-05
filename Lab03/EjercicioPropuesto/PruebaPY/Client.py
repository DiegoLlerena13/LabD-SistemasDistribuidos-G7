import socket
import threading
import pickle
from chat_message import ChatMessage, MESSAGE, LOGOUT, WHOISIN

class Client:
    def __init__(self, host='localhost', port=1500, username='Anonimo'):
        self.host = host
        self.port = port
        self.username = username
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def start(self):
        try:
            self.socket.connect((self.host, self.port))
            self.socket.send(pickle.dumps(self.username))
        except:
            print("Error al conectar con el servidor.")
            return

        threading.Thread(target=self.listen_from_server, daemon=True).start()

        print("Conectado al chat. Comandos disponibles: LOGOUT, WHOISIN, @usuario mensaje, mensaje normal.")
        while True:
            msg = input("> ")
            if msg.upper() == "LOGOUT":
                self.send(ChatMessage(LOGOUT))
                break
            elif msg.upper() == "WHOISIN":
                self.send(ChatMessage(WHOISIN))
            else:
                self.send(ChatMessage(MESSAGE, msg))
        self.socket.close()

    def send(self, message):
        try:
            self.socket.send(pickle.dumps(message))
        except:
            print("Error al enviar mensaje.")

    def listen_from_server(self):
        while True:
            try:
                data = self.socket.recv(4096)
                if not data:
                    break
                msg = pickle.loads(data)
                print(msg)
            except:
                break

if __name__ == "__main__":
    username = input("Ingrese su nombre de usuario: ")
    Client(username=username).start()
