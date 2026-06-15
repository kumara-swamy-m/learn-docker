from flask import Flask
import mysql.connector
import time

app = Flask(__name__)

def get_connection():
    while True:
        try:
            conn = mysql.connector.connect(
                host="db",
                user="root",
                password="root"
            )
            return conn
        except Exception as e:
            print("Waiting for database...", e)
            time.sleep(5)

@app.route("/")
def home():
    conn = get_connection()
    return "Flask connected to MySQL successfully!"

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)

