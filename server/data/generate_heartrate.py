import datetime
import random
import json
import sys

# Define the starting timestamp
start_timestamp = datetime.datetime.strptime("2024-01-01T00:00:00Z", "%Y-%m-%dT%H:%M:%SZ")

# Function to generate random heart rate data
def generate_heart_rate():
    sand = random.randint(0,20)
    return random.randint(50+sand, 70+sand)  # Assuming normal heart rate range

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
                    "heart_rate": str(generate_heart_rate())
                })
    filename = "patient_" + patient_id + "_heart_rate_data.json"

    # Output the generated data to a JSON file
    with open(filename, "w") as file:
        json.dump(data, file, indent=2)

    print("Data generated successfully and saved to",filename)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script_name.py <patient_id>")
        sys.exit(1)
    
    patient_id = sys.argv[1]
    generate_data(patient_id)
