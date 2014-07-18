//Web Service Settings
TCPClient client;
byte server[] = { 74, 125, 224, 72 };

//Load Cell Calibration Settings
float aReading = 800.0;  
float aLoad = 10; //kg
float bReading = 4000.0;
float bLoad = 50; //kg
float previousLoad = 0.975;	//Initialized to weight (kg) with no items placed

//Initialize OPS Regions
String aout = "";
int const PHOTORESISTORS = 15;
boolean previousOPSRegions[PHOTORESISTORS];
boolean currentOPSRegions[PHOTORESISTORS];
int tolerance = 1000;	
int r0 = 0;             //value of select pin at (s0)
int r1 = 0;             //value of select pin at (s1)
int r2 = 0;             //value of select pin at (s2)
int r3 = 0;             //value of select pin at (s3)

void setup() {

	//Assign baud rate
	Serial.begin(9600);

	//Assign I/O pins
	pinMode(A0, INPUT);
	pinMode(A1, INPUT);
	pinMode(D0, OUTPUT);
	pinMode(D1, OUTPUT);
	pinMode(D2, OUTPUT);
	pinMode(D3, OUTPUT);

	//Initialize OPS Region to empty
	for (int region=0; region<PHOTORESISTORS; region++) {
		previousOPSRegions[region] = false;
	}

}

void sendWebService(String methodType, String JSON_OPSvalues, float weight) {
	String JSON;
	String weightDifference = String(weight);

	//Create request details
	if (methodType.equals("itemIn")) {
		JSON = "method=itemIn&username=test&password=test&ShelfId=1&ShelfRegions=" + JSON_OPSvalues + "&Amount=" + weightDifference;	
	}
	else {
		JSON = "method=itemOut&username=test&password=test&ShelfId=1&ShelfRegions=" + JSON_OPSvalues;
	}

	//Connect to Web Service
	if (client.connect("shelfe.netau.net", 80)) {

		Serial.println("Connected!");
		Serial.println("Sending JSON string: " + JSON);

		client.println("GET /service/Service.php?" + JSON + " HTTP/1.1");
		client.println("Host: shelfe.netau.net");
		client.println("Cache-Control: no-cache");
		client.println();
	}

	else
	{
		Serial.println("Connection failed");
	}
}

void loop() {

	int OPSvalue;
	float LCvalue;
	float currentLoad;
	float weightDifference;
	String JSON_OPSvalues = "";
	boolean updateRequired = false;

	//*******************
	//PHOTORESISTOR CODE
	//********************
	for (int count=0; count<PHOTORESISTORS; count++) {

		//Perform bit shifting for select lines
		r0 = count & 0x01;      
		r1 = (count>>1) & 0x01; 
		r2 = (count>>2) & 0x01; 
		r3 = (count>>3) & 0x01; 

		//Write to select lines
		digitalWrite(D0, r0);
		digitalWrite(D1, r1);
		digitalWrite(D2, r2);
		digitalWrite(D3, r3);
		delay(50);

		//Read photoresistor value
		OPSvalue = analogRead(A0);
		if (OPSvalue < tolerance) {
			aout += "X ";
			currentOPSRegions[count] = true;
		}
		else {
			aout += "- ";
			currentOPSRegions[count] = false;
		}

		//Format output
		if (count % 3 == 2) {
			Serial.println(aout);
			aout = "";
		}
		if (count % 14 == 0) {
			Serial.println("-----------");
		}

	}

	//***************
	//LOAD CELL CODE
	//***************
	LCvalue = analogRead(A1);
	currentLoad = ((bLoad - aLoad)/(bReading - aReading))*(LCvalue - aReading) + aLoad;
	//RE-ENABLE THIS!
	Serial.print("Load Cell Reading: ");
	Serial.print(LCvalue);
	Serial.print("Load: ");
	Serial.println(currentLoad, 5);

	//Send data to Web Service because new item was placed
	//Condition: Weight increased
	//Procedure: 
	//1. Find newly occupied regions
	//2. Issue GET request with weight difference and newly occupied regions
	//3. Update previousLoad and previousOPSRegion
	if (currentLoad > previousLoad) {

		Serial.println("Condition Satisfied: Current Load > Previous Load");
		Serial.println(currentLoad, 5);
		Serial.println(previousLoad, 5);

		//Find newly occupied regions
		for (int region=0; region<PHOTORESISTORS; region++) {
			if (previousOPSRegions[region] != currentOPSRegions[region]) {
				JSON_OPSvalues += String(region) + ",";
				updateRequired = true;
			}
		}

		if(updateRequired) {
			//Delete the last comma
			String hello = JSON_OPSvalues.substring(0, JSON_OPSvalues.length() - 1);

			Serial.print("JSON_OPS: ");
			Serial.println(hello);

			//Issue GET request with weight differences and new occupied region
			weightDifference = currentLoad - previousLoad;
			sendWebService("itemIn", hello, weightDifference);

			//Update previousLoad and previousOPSRegions
			previousLoad = currentLoad;
			for (int region = 0; region<PHOTORESISTORS; region++) {
				previousOPSRegions[region] = currentOPSRegions[region];
			}	
		}
	}

	//Send data to Web Service because item was taken out
	//Condition: Weight decreased
	//Procedure: 
	//1. Find newly occupied regions
	//2. Issue GET request with newly unoccupied regions
	//3. Update previousLoad and previousOPSRegion
	else if (currentLoad < previousLoad) {
		Serial.println("Condition Satisfied: Current Load < Previous Load");
		Serial.println(currentLoad, 5);
		Serial.println(previousLoad, 5);

		//Find newly unoccupied regions
		for (int region=0; region<PHOTORESISTORS; region++) {
			if (previousOPSRegions[region] != currentOPSRegions[region]) {
				JSON_OPSvalues += String(region) + ",";
				updateRequired = true;
			}
		}

		if(updateRequired) {
			//Delete the last comma
			String hello2 = JSON_OPSvalues.substring(0, JSON_OPSvalues.length() - 1);

			Serial.print("JSON_OPS: ");
			Serial.println(hello2);

			//Issue GET request with newly unoccupied regions
			sendWebService("itemOut", hello2, 0);

			//Update previousLoad and previousOPSRegions
			previousLoad = currentLoad;
			for (int region = 0; region<PHOTORESISTORS; region++) {
				previousOPSRegions[region] = currentOPSRegions[region];
			}
		}
	}

	//Read from Web Service
	while (client.available()) {
		char c = client.read();
		Serial.print(c);
	}

	
	//Clean-up
	//delay(300);	//Wait for fluctuations to settle before polling sensors again

}
