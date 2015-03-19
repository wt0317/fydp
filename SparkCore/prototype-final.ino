// This #include statement was automatically added by the Spark IDE.
#undef min
#undef max
#include <queue>
#include "math.h"

std::queue<String> requests;

//Web Service Settings
TCPClient client;
String const ID = "2015";
String const PASSWORD = "ACY778zUguizA";

//Load Cell Calibration Settings
float aReading = 622.0;
float aLoad = 6.86; //kg
float bReading = 735.0;
float bLoad = 2.26; //kg
int previous_LCvalue;
int current_LCvalue;
float const THRESHOLD = 0.08;

//OPS Settings
String aout = "";
int const NUM_OPS_PER_REGION = 15;
int const NUM_OPS = 45;
int const REPEAT = 5;
int const TOLERANCE = 1000; //630
int previous_regions[NUM_OPS];
int current_regions[NUM_OPS];

void setup() {

	//Assign baud rate and pin modes
    Serial.begin(9600);
    pinMode(A0, INPUT); //photoresistors(left)
    pinMode(A1, INPUT); //photoresistors(center)
    pinMode(A2, INPUT); //photoresistors(right)
    pinMode(A3, INPUT); //weightsensor
    pinMode(D4, OUTPUT);
    pinMode(D5, OUTPUT);
    pinMode(D6, OUTPUT);
    pinMode(D7, OUTPUT);
    delay(1000);

	//Web registration
	registerShelf();

	//Initialize OPS
    getStabilizedOPS();
    for (int region = 0; region<NUM_OPS; region++) {
			previous_regions[region] = current_regions[region];
	}
	//previousWeight = getStabilizedWeight(500);
	previous_LCvalue = getStabilizedLCvalue(500);
}

void registerShelf() {

    String msg = "method=registerShelf&ShelfId=";
    msg.concat(ID);
    msg.concat("&password=");
    msg.concat(PASSWORD);

    requests.push(msg);
    sendWebService();
}

void sendWebService() {

    boolean retryRequired = true;
	boolean serverReplied = false;
	String header;
	String content;

	//Send all web requests in queue
    do {
        content = requests.front();
        //Connect to Web Service
    	if (client.connect("shelfe.host22.com", 80)) {

    	        header = "POST /service/Service.php HTTP/1.1";
    		    header.concat("\nHost: shelfe.host22.com");
    		    header.concat("\nContent-Type: application/x-www-form-urlencoded");
    		    header.concat("\nCache-Control: no-cache");
    		    header.concat("\nContent-Length: ");
    		    header.concat(content.length());
    		    header.concat("\n\n");
    		    header.concat(content);

    			Serial.println();
    			Serial.println(header);
    			client.println(header);
    			client.flush();

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
        else {
        	    Serial.println("Failed to connect to Wi-Fi");
        }

        //Don't send the next request in the queue because current request failed
        //and we need to maintain order of requests sent
        if(retryRequired == true) {
            Serial.println("Batched!");
            Serial.print("Queue Size: ");
            Serial.println(requests.size());
            break;
        }
        //Remove msg at front of queue because it has been processed
        if(retryRequired == false) {
            requests.pop();
            Serial.println("Sent!");
            Serial.print("Queue Size: ");
            Serial.println(requests.size());
        }
    }while(!requests.empty());
}

void getStabilizedOPS() {
    int analog_read_region_left;
    int analog_read_region_center;
    int analog_read_region_right;
	int region_left[16];
    int region_center[16];
    int region_right[16];
    int map_region[NUM_OPS] = {3,4,6,1,2,11,5,7,0,13,10,9,8,15,14,4,6,7,0,1,5,10,8,15,2,12,11,9,14,13,5,6,7,0,1,4,10,8,15,2,12,11,9,14,13};

    // int count=0;
    String out;

    //Read photoresistors
	for (int i = 0; i < REPEAT; i++) {
		for (int j = 0; j < 16; j++) {

            digitalWrite(D4, j & 0x01);
            digitalWrite(D5, (j>>1) & 0x01);
            digitalWrite(D6, (j>>2) & 0x01);
            digitalWrite(D7, (j>>3) & 0x01);
            delay(5);
            analog_read_region_left = analogRead(A0);
            delay(5);
            analog_read_region_center = analogRead(A1);
            delay(5);
            analog_read_region_right = analogRead(A2);


            //Set regions to 1 or 0 (1 = object, 0 = no object)
            if (i==1) {
                region_left[j] = analog_read_region_left < TOLERANCE;

                region_center[j] = analog_read_region_center < TOLERANCE;

                region_right[j] = analog_read_region_right < TOLERANCE;
            }


            //Remove shadow bias
            else {
                if (analog_read_region_left > TOLERANCE) {
    			    region_left[j] = 0;

                }
                if (analog_read_region_center > TOLERANCE) {
                    region_center[j] = 0;

                }
                if(analog_read_region_right > TOLERANCE) {
                    region_right[j] = 0;

                }

            }
		}
	}

	//Re-map photoresistors to single region
	for (int i = 0; i < 15; i++) {
        current_regions[i] = region_left[map_region[i]];
    }

    for (int i = 15; i < 30; i++) {
        current_regions[i] = region_center[map_region[i]];
    }

    for (int i = 30; i < 45; i++) {
        current_regions[i] = region_right[map_region[i]];
    }

    //Serial.println(String(count++) + "------------------------------");
     //for (int i = 0; i < NUM_OPS; i++) {
       // if (i != 0 && i%5 == 0) {
         //   Serial.println(out);
           // out = "";
        //}
        // out += "(" + String(map_region[i]) + ")" + String(int(regions[i])) + " ";
        //out += String(int(current_regions[i])) + " ";
    //}
    //Serial.println(out);
    //out = "";
}

//Get a stable weight value
float getStabilizedLCvalue(int repetition) {
	Serial.println("Stabilizing LCvalue...");

	int maxValue;
    int minValue;
    do {
        maxValue = 0;
        minValue = 5000;
        for (int i = 0; i < 500; i++) {
            int value = analogRead(A3);
            if (value > maxValue) {
                maxValue = value;
            }
            if (value < minValue) {
                minValue = value;
            }
        }
    } while (maxValue - minValue > 4);

	//float currentWeight;
	int count;
	int LCvalue = 0;

	//Get stable analog value
	for (count=0 ; count < repetition; count++) {
	    LCvalue += analogRead(A3);
	}
	LCvalue = LCvalue/repetition;

	//currentWeight = ((bLoad - aLoad)/(bReading - aReading))*(LCvalue - aReading) + aLoad;

	return LCvalue;
// 	float currentWeight;
// 	int LCvalues[repetition];
// 	int max_LCvalue;
// 	int min_LCvalue;
// 	int stable_LCvalue = 0;
// 	int temp_LCvalue;

// 	do {
// 	    max_LCvalue=0;
// 	    min_LCvalue=4096;
// 	    temp_LCvalue=0;
//     	for (int count=0 ; count < repetition; count++) {
//     	    Serial.println("Count: " + String(count));
//     	    for (int sample = 0; sample < 500; sample++) {
//                 temp_LCvalue += analogRead(A3);
//             }
//             temp_LCvalue = temp_LCvalue/500;
//     	    LCvalues[count] = temp_LCvalue;

//     	    Serial.println(LCvalues[count]);
//     	    if(LCvalues[count] > max_LCvalue) {
//     	        max_LCvalue = LCvalues[count];
//     	    }
//     	    if(LCvalues[count] < min_LCvalue) {
//     	        min_LCvalue = LCvalues[count];
//     	    }
//     	}
//     	Serial.println("Max LCvalue: " + String(max_LCvalue));
//     	Serial.println("Min LCvalue: " + String(min_LCvalue));
// 	}while(max_LCvalue - min_LCvalue > 2);

// 	//Get stable LCvalue
// 	Serial.print("Stable LCvalue:");
// 	Serial.println(stable_LCvalue);
// 	stable_LCvalue = (min_LCvalue + max_LCvalue) / 2;

// 	currentWeight = ((bLoad - aLoad)/(bReading - aReading))*(stable_LCvalue - aReading) + aLoad;
// 	Serial.println("Done Stabilizing weight!");
// 	return currentWeight;



}

void loop() {

	float current_weight, previous_weight, weight_difference;
	String OPSvalues = "";
	String msg = "";
	boolean updateRequired = false;

    if(!requests.empty()) {
        sendWebService();
    }

	//***************
	//LOAD CELL CODE
	//***************
	//currentWeight = getStabilizedWeight(500);
	current_LCvalue = getStabilizedLCvalue(500);
	Serial.print("Current LCvalue: ");
	Serial.println(current_LCvalue);
	Serial.print("Previous LCvalue: ");
	Serial.println(previous_LCvalue);

	//Send data to Web Service because new item was placed
	//Condition: Weight increased
	//Procedure:
	//1. Find newly occupied regions
	//2. Issue GET request with weight difference and newly occupied regions
	//3. Update previousWeight and previousOPSRegion
	if (previous_LCvalue - current_LCvalue > 2) {

		getStabilizedOPS();
		current_LCvalue = getStabilizedLCvalue(500);


		//Find newly occupied regions
		for (int region=0; region<NUM_OPS; region++) {
			if (previous_regions[region] != current_regions[region] && current_regions[region]) {
				OPSvalues += String(region) + ",";
				updateRequired = true;
			}
		}

		if(updateRequired) {

			//Delete the last comma
			String temp = OPSvalues.substring(0, OPSvalues.length() - 1);
			current_weight = ((bLoad - aLoad)/(bReading - aReading))*(current_LCvalue - aReading) + aLoad;
			previous_weight = ((bLoad - aLoad)/(bReading - aReading))*(previous_LCvalue - aReading) + aLoad;
			weight_difference = current_weight - previous_weight;
			OPSvalues = temp;

			//Issue POST request with weight differences and new occupied region
			//weightDifference = currentWeight - previousWeight;
			msg = "method=itemIn&ShelfId=";
	        msg.concat(ID);
	        msg.concat("&ShelfPassword=");
	        msg.concat(PASSWORD);
	        msg.concat("&ShelfRegions=");
	        msg.concat(OPSvalues);
	        msg.concat("&Amount=");
	        msg.concat(weight_difference);
	        requests.push(msg);
			sendWebService();

			//Update previousWeight and previous_regions
			previous_LCvalue = current_LCvalue;
			for (int region = 0; region<NUM_OPS; region++) {
				previous_regions[region] = current_regions[region];
			}
		}
	}

	//Send data to Web Service because item was taken out
	//Condition: Weight decreased
	//Procedure:
	//1. Find newly occupied regions
	//2. Issue GET request with newly unoccupied regions
	//3. Update previousWeight and previousOPSRegion
	else if (current_LCvalue - previous_LCvalue > 2) {

		getStabilizedOPS();
		current_LCvalue = getStabilizedLCvalue(500);

		//Find newly unoccupied regions
		for (int region=0; region<NUM_OPS; region++) {
			if (previous_regions[region] != current_regions[region] && !current_regions[region]) {
				OPSvalues += String(region) + ",";
				updateRequired = true;
			}
		}

		if(updateRequired) {

			//Delete the last comma
			String temp = OPSvalues.substring(0, OPSvalues.length() - 1);
			OPSvalues = temp;

			//Issue POST request with newly unoccupied regions
			msg = "method=itemOut&ShelfId=";
	        msg.concat(ID);
	        msg.concat("&ShelfPassword=");
	        msg.concat(PASSWORD);
	        msg.concat("&ShelfRegions=");
	        msg.concat(OPSvalues);
	        requests.push(msg);
			sendWebService();

			//Update previousWeight and previous_regions
			previous_LCvalue = current_LCvalue;
			for (int region = 0; region<NUM_OPS; region++) {
				previous_regions[region] = current_regions[region];
			}
		}
	}

	//delay(300);	//Wait for fluctuations to settle before polling sensors again
}
