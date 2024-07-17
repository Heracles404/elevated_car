from flask import Flask

app = Flask(__name__)

# Configure the template folder to point to app/view/templates
app.template_folder = "view/templates"

from app.controller import routes
