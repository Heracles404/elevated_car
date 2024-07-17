from connect import get_db_connection

def generate_appID():
    import random
    import string
    return ''.join(random.choices(string.ascii_lowercase + string.digits, k=8))

def insert_car_movement(appID, origin, destination):
    connection = get_db_connection()
    cursor = connection.cursor()

    sql = "INSERT INTO car_movement (appID, origin, destination) VALUES (%s, %s, %s)"
    cursor.execute(sql, (appID, origin, destination))
    connection.commit()
    cursor.close()
    connection.close()

def get_car_movement_status(appID):
    connection = get_db_connection()
    cursor = connection.cursor()

    sql = "SELECT origin FROM car_movement WHERE appID = %s"
    cursor.execute(sql, (appID,))
    result = cursor.fetchone()

    if result:
        origin = result[0]
        if origin == '0':
            status_message = "On Board"
        else:
            status_message = "Waiting"
    else:
        status_message = "Arrived"

    cursor.close()
    connection.close()

    return status_message
