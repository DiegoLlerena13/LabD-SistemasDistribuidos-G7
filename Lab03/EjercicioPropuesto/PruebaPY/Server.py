import socket
import threading
import pickle
from chat_message import ChatMessage, MESSAGE, LOGOUT, WHOISIN

class Server:
    def __init__(self, host='localhost', port=1500):
        self.clients = []
        self.host = host
        self.port = port
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def start(self):
        self.server_socket.bind((self.host, self.port))
        self.server_socket.listen()
        print(f"Servidor escuchando en {self.host}:{self.port}")
        while True:
            client_socket, addr = self.server_socket.accept()
            threading.Thread(target=self.handle_client, args=(client_socket,), daemon=True).start()

    def broadcast(self, message, exclude_socket=None):
        for client in self.clients:
            if client['socket'] != exclude_socket:
                try:
                    client['socket'].send(pickle.dumps(message))
                except:
                    client['socket'].close()
                    self.clients.remove(client)

    def handle_client(self, client_socket):
        try:
            username = pickle.loads(client_socket.recv(1024))
        except:
            client_socket.close()
            return

        client_info = {'socket': client_socket, 'username': username}
        self.clients.append(client_info)
        self.broadcast(f"*** {username} ha ingresado al chat ***")

        while True:
            try:
                data = client_socket.recv(4096)
                if not data:
                    break
                msg = pickle.loads(data)

                if msg.type == MESSAGE:
                    self.broadcast(f"{username}: {msg.message}", exclude_socket=client_socket)
                elif msg.type == WHOISIN:
                    user_list = "Usuarios conectados:\n" + "\n".join(c['username'] for c in self.clients)
                    client_socket.send(pickle.dumps(user_list))
                elif msg.type == LOGOUT:
                    break
            except:
                break

        self.clients.remove(client_info)
        self.broadcast(f"*** {username} ha salido del chat ***")
        client_socket.close()

if __name__ == "__main__":
    Server().start()
