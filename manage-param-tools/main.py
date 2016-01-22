
import json
from flask import Flask, request, render_template, redirect

CONF_JSON_FILE_PATH = './config/allow_params.json'
app = Flask(__name__)


def load_conf():
    with open(CONF_JSON_FILE_PATH) as f:
        json_obj = json.load(f)
    return json_obj




if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True)