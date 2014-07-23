#include "math.h"

//Web Service Settings
TCPClient client;

//Load Cell Calibration Settings
float const EPSILON = 0.01;
float aReading = 819.2;  
float aLoad = 10; //kg
float bReading = 4096.0;
float bLoad = 50; //kg
float previousWeight;	//Initialized to weight (kg) with no items placed
float const THRESHOLD = 0.08;

//Initialize OPS Regions
String aout = "";
int const PHOTORESISTORS = 15;
int tolerance = 800; //630	
int r0 = 0;             //value of select pin at (s0)
int r1 = 0;             //value of select pin at (s1)
int r2 = 0;             //value of select pin at (s2)
int r3 = 0;             //value of select pin at (s3)
boolean previousOPSRegions[PHOTORESISTORS];
boolean currentOPSRegions[PHOTORESISTORS];

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
	
	delay(5000);
	
    getStabilizedOPS();
    for (int region = 0; region<PHOTORESISTORS; region++) {
			previousOPSRegions[region] = currentOPSRegions[region];
	}
	previousWeight = getStabilizedWeight(20);

}

void sendWebService(String methodType, String JSON_OPSvalues, float weight) {
	String JSON;
	String weightDifference = String(weight);
	boolean retryRequired = true;
	int retryCount = 0;
	bool serverReplied = false;

	//Create request details
	if (methodType.equals("itemIn")) {
		JSON = "method=itemIn&username=test&password=test&ShelfId=1&ShelfRegions=" + JSON_OPSvalues + "&Amount=" + weightDifference;	
	}
	else {
		JSON = "method=itemOut&username=test&password=test&ShelfId=1&ShelfRegions=" + JSON_OPSvalues;
	}

	//Connect to Web Service
	do {
		if (client.connect("shelfe.netau.net", 80)) {

			Serial.println("Connected!");
			Serial.println("Sending JSON string: " + JSON);

			client.println("GET /service/Service.php?" + JSON + " HTTP/1.1");
			client.println("Host: shelfe.netau.net");
			client.println("Cache-Control: no-cache");
			client.println();

			while (!serverReplied) {
    			//Read from Web Service
    			while (client.available()) {
    				char c = client.read();
    				Serial.print(c);
    				serverReplied = true;
    			}
    		}
    		client.stop();
    		retryRequired = false;
    	}

    	else
    	{
    		delay(250);
    		retryCount++;
    		Serial.println("Connection failed.");
    		Serial.print("Retrying: ");
    		Serial.println(retryCount);
    	}

    	}while(retryRequired == true && retryCount < 6);
    }

//Read all photoresistors and ensure stability
void getStabilizedOPS() {
	int OPSvalue;
// 	boolean temporaryOPSRegions[2][PHOTORESISTORS];

	for (int count = 0; count < 4; count++) {
		for (int region=0; region<PHOTORESISTORS; region++) {

			//Perform bit shifting for select lines
			r0 = region & 0x01;      
			r1 = (region>>1) & 0x01; 
			r2 = (region>>2) & 0x01; 
			r3 = (region>>3) & 0x01; 

			//Write to select lines
			digitalWrite(D0, r0);
			digitalWrite(D1, r1);
			digitalWrite(D2, r2);
			digitalWrite(D3, r3);
			delay(50);

            if (count == 1) {
    			//Read photoresistor value
    			OPSvalue = analogRead(A0);
    			if (OPSvalue < tolerance) {
    				//aout += "X ";
    				currentOPSRegions[region] = true;
    			}
    			else {
    				//aout += "- ";
    				currentOPSRegions[region] = false;
    			}
            } else {
                //Read photoresistor value
    			OPSvalue = analogRead(A0);
    			if (OPSvalue > tolerance) {
    				//aout += "X ";
    				currentOPSRegions[region] = false;
    			}
            }

			//Format output
// 			aout += "-" + String(OPSvalue) + "- ";
// 			if (count % 3 == 2) {
// 				//Serial.println(aout);
// 				aout = "";
// 			}
// 			if (count % 14 == 0) {
// 				//Serial.println("-----------");
// 			}
		}

        
		//Compare currentOPSRegions to previous
// 		if(count > 0) {
// 		    Serial.println("Comparing...");
// 			for (int region = 0; region<PHOTORESISTORS; region++) {
// 				if(temporaryOPSRegions[count-1][region] != temporaryOPSRegions[count][region]) {
// 					count = -1;
// 					break;
// 				}
// 			}
// 			Serial.println("Comparing DONE!");
// 		}
	}

	//Copy stabilized temporaryOPSRegions to currentOPSRegions
// 	for (int region = 0; region < PHOTORESISTORS; region++) {
// 		currentOPSRegions[region] = temporaryOPSRegions[1][region];	
// 	}

}

//Get a stable weight value
//Condition: 5 consecutive weight values
float getStabilizedWeight(int repetition) {
	float LCvalue;
	float currentWeights[repetition];
	
	int count;
	for (count = 0 ; count < arraySize(currentWeights); count++) {
		LCvalue = analogRead(A1);
		
		currentWeights[count] = ((bLoad - aLoad)/(bReading - aReading))*(LCvalue - aReading) + aLoad;
		if (count > 0) {
		  //  String info = String(count) + " " + String(currentWeights[count]) + " " + String(fabs(currentWeights[count]-currentWeights[count-1]));
    //         Serial.println(info);
		    if (fabs(currentWeights[count]-currentWeights[count-1]) > EPSILON) {
			    count = -1;
		    }
		}
	}
	
	return currentWeights[4];
}

void loop() {

	float LCvalue;
	float currentWeight;
	float weightDifference;
	String JSON_OPSvalues = "";
	boolean updateRequired = false;

	//***************
	//LOAD CELL CODE
	//***************
	currentWeight = getStabilizedWeight(20);
	Serial.print("Weight: ");
	Serial.println(currentWeight, 5);
	Serial.print("Previous Value: ");
	Serial.println(previousWeight, 5);

	//Send data to Web Service because new item was placed
	//Condition: Weight increased
	//Procedure: 
	//1. Find newly occupied regions
	//2. Issue GET request with weight difference and newly occupied regions
	//3. Update previousWeight and previousOPSRegion
	if (currentWeight > previousWeight + THRESHOLD) {

		Serial.println("Item was placed!");

		Serial.println("Stabilizing OPS...");
		getStabilizedOPS();
		
		currentWeight = getStabilizedWeight(50);

		//Find newly occupied regions
		for (int region=0; region<PHOTORESISTORS; region++) {
			if (previousOPSRegions[region] != currentOPSRegions[region] && currentOPSRegions[region]) {
				JSON_OPSvalues += String(region) + ",";
				updateRequired = true;
			}
		}

		if(updateRequired) {

			//Delete the last comma
			String hello = JSON_OPSvalues.substring(0, JSON_OPSvalues.length() - 1);

			//Serial.print("JSON_OPS: ");
			//Serial.println(hello);

			//Issue GET request with weight differences and new occupied region
			weightDifference = currentWeight - previousWeight;
			sendWebService("itemIn", hello, weightDifference);

			//Update previousWeight and previousOPSRegions
			previousWeight = currentWeight;
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
	//3. Update previousWeight and previousOPSRegion
	else if (currentWeight < previousWeight - THRESHOLD) {
		Serial.println("Item was taken out!");

		Serial.println("Stabilizing OPS...");
		getStabilizedOPS();
		
		currentWeight = getStabilizedWeight(50);

		//Find newly unoccupied regions
		for (int region=0; region<PHOTORESISTORS; region++) {
			if (previousOPSRegions[region] != currentOPSRegions[region] && !currentOPSRegions[region]) {
				JSON_OPSvalues += String(region) + ",";
				updateRequired = true;
			}
		}

		if(updateRequired) {

			//Delete the last comma
			String hello2 = JSON_OPSvalues.substring(0, JSON_OPSvalues.length() - 1);

			//Serial.print("JSON_OPS: ");
			//Serial.println(hello2);

			//Issue GET request with newly unoccupied regions
			sendWebService("itemOut", hello2, 0);

			//Update previousWeight and previousOPSRegions
			previousWeight = currentWeight;
			for (int region = 0; region<PHOTORESISTORS; region++) {
				previousOPSRegions[region] = currentOPSRegions[region];
			}
		}
	}

	//Clean-up
	//delay(300);	//Wait for fluctuations to settle before polling sensors again

}