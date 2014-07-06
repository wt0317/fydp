

void setup() 
{
  Serial.begin(9600);
  pinMode(10, OUTPUT);      // sets the digital pin as output
  pinMode(11, OUTPUT);      // sets the digital pin as output
  pinMode(12, OUTPUT);      // sets the digital pin as output
  pinMode(13, OUTPUT);      // sets the digital pin as output
}

void loop() {
   int sensorValueA0 = 0;  
   selectOPS1(); 
//   delay(1);
   sensorValueA0 = analogRead(0);
   Serial.print("A0: ");Serial.print(sensorValueA0);
//   delay(1000); 
   selectOPS2();
//   delay(1);
   sensorValueA0 = analogRead(0);
   Serial.print(" A1: ");Serial.println(sensorValueA0);
//   delay(1000);
  
}


void selectOPS1()
{
  digitalWrite(10, LOW);  
  digitalWrite(11, LOW);
  digitalWrite(12, LOW);
  digitalWrite(13, LOW);
}

void selectOPS2()
{
  digitalWrite(10, HIGH);
  digitalWrite(11, LOW);
  digitalWrite(12, LOW);
  digitalWrite(13, LOW);
}
