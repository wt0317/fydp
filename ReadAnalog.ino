int sensorPin = A0;    // select the input pin for the potentiometer
int sensorValue = 0;  // variable to store the value coming from the sensor
int prevValue = 0;

void setup() {
  Serial.begin(9600);
}

void loop() {
  sensorValue = analogRead(sensorPin);
  if (prevValue != sensorValue) {
    Serial.println(50000/(5/1024*sensorValue)-10000);
    prevValue = sensorValue;
  }
  delay(1000);
}
