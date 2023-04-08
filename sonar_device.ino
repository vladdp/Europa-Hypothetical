const int trigPin[] = {2, 4, 6, 8, 10}; // arduino pins used for triggering sensors
const int echoPin[] = {3, 5, 7, 9, 11}; // arduino pins used to read echo value from sensors
float sensor[5]; // average of 3 distance readings
float distance[5]; // stores distance reading from sensor

int x = 0; // the current x position
int y = 0; // the current y position

float frontSensor;
float leftSensor;
float topSensor;
float bottomSensor1;
float bottomSensor2;

float startingHeight = 0;
float heightCorrection; // difference between starting height and topSensor

boolean start = false; // becomes true after setup() is executed

int count = 5; // number of calibrations before printing values

void setup() { 
  Serial.begin (9600);

  // This loop initializes all the input and output pins
  for (int i = 0; i < 5; i++ ) {
    pinMode(trigPin[i], OUTPUT);
    pinMode(echoPin[i], INPUT);
  }

  // Calibrate the sensors
  for (int i = 0; i < count; i++) {
    checkDistance();
  }

  // set values to store previous sensor readings
  frontSensor = sensor[0];
  leftSensor = sensor[1];
  topSensor = sensor[2];
  bottomSensor1 = sensor[3];
  bottomSensor2 = sensor[4];

  startingHeight = topSensor;
  start = true;

  printData();
}

void loop() {
  checkDistance();

  // the if statements check to see if the Arduino moved one cm in either x or y dir
  if (abs(sensor[0] - frontSensor) >= 1 && abs(sensor[1] - leftSensor) >= 1) {
    if (frontSensor > sensor[0]) y++;
    else y--;
    frontSensor = sensor[0];

    if (leftSensor < sensor[1]) x++;
    else x--;
    leftSensor = sensor[1];

    printData();

  } else if (abs(sensor[0] - frontSensor) >= 1) {
    if (frontSensor > sensor[0]) y++;
    else y--;
    frontSensor = sensor[0];

    printData();

  } else if (abs(sensor[1] - leftSensor) >= 1) {
    if (leftSensor < sensor[1]) x++;
    else x--;
    leftSensor = sensor[1];

    printData();
  }
  
  delay(10);
}

void checkDistance() {
  for (int i = 0; i < 5; i++) {
    sensor[i] = 0;
  }

  for (int x = 0; x < 3; x++) {
    for (int i = 0; i < 5; i++) {
      digitalWrite(trigPin[i], LOW);
      delayMicroseconds(2);
      digitalWrite(trigPin[i], HIGH);
      delayMicroseconds(10);
      digitalWrite(trigPin[i], LOW);

      distance[i] = pulseIn(echoPin[i], HIGH) / 58.00;
      sensor[i] += distance[i];

      delay(120);
    }
  }

  // average the distance value using 3 readings
  for (int i = 0; i < 5; i++) {
    sensor[i] = sensor[i] / 3;
  }

  // calculate the height correction
  if (start) {
    heightCorrection = sensor[2] - startingHeight;
  }
}

void printData() {
  Serial.print(x);
  Serial.print("\t");
  Serial.print(y);
  Serial.print("\t");
  Serial.print(sensor[3] + heightCorrection);
  Serial.print("\t");
  Serial.print(sensor[4] + heightCorrection);
  Serial.println();
}
