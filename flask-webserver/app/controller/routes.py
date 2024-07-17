# app/routes.py
from flask import render_template, request, redirect, url_for, jsonify
from app import app
from app.model.car_movement import generate_appID, insert_car_movement, get_car_movement_status

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        origin = request.form.get('origin')
        destination = request.form.get('destination')

        if origin == destination:
            return render_template('index.html', error="Origin and Destination must be different.")

        appID = generate_appID()
        insert_car_movement(appID, origin, destination)

        # Redirect to status page with appID
        return redirect(url_for('get_status', appID=appID))

    return render_template('index.html')

@app.route('/get_status/<appID>', methods=['GET'])
def get_status(appID):
    return render_template('status.html', appID=appID)  # Pass appID to the template

@app.route('/api/get_status/<appID>', methods=['GET'])
def api_get_status(appID):
    status_message = get_car_movement_status(appID)
    return jsonify({'status': status_message})  # Return JSON response