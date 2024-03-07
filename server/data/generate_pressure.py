import datetime
import random
import json
import sys

# Define the starting timestamp
start_timestamp = datetime.datetime.strptime("2024-01-01T00:00:00Z", "%Y-%m-%dT%H:%M:%SZ")

# Function to generate random sbp
def generate_sbp():
    sand = random.randint(0,10)
    return random.randint(112+sand, 125+sand)  # Assuming normal heart rate range

# Function to generate random dbp
def generate_dbp():
    sand = random.randint(0,10)
    return random.randint(74+sand, 86+sand)  # Assuming normal heart rate range

# Generate data for 5 days
def generate_data(patient_id):
    data = []
    for day in range(5):
        for hour in range(24):
            for minute in range(0, 60, 15):  # Generate data every 15 minutes
                timestamp = start_timestamp + datetime.timedelta(days=day, hours=hour, minutes=minute)
                data.append({
                    "patient_id": patient_id,
                    "timestamp": timestamp.strftime("%Y-%m-%dT%H:%M:%SZ"),
                    "systolic_pressure": str(generate_sbp()),
                    "diastolic_pressure": str(generate_dbp()),
                })
    filename = "patient_" + patient_id + "_pressure_data.json"
    # Output the generated data to a JSON file
    with open(filename, "w") as file:
        json.dump(data, file, indent=2)

    print("Data generated successfully and saved to", filename)


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script_name.py <patient_id>")
        sys.exit(1)
    
    patient_id = sys.argv[1]
    generate_data(patient_id)
