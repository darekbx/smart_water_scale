#include <M5Atom.h>
#include <FastLED.h>
#include "HX711.h"

#include "storage.h"

#define DEBUG true
#define SCALE_TIMES_AVERAGE 5
#define THREAD_DELAY 3000
#define COMPARE_DELTA 10

#define LOADCELL_DOUT_PIN 32
#define LOADCELL_SCK_PIN  26
#define PIN_LEDATOM 27

HX711 scale;
Storage storage;

int prevWeight = 0;
bool deviceConnected = false;

void setup() {
  M5.begin(true, false, true);

  storage.init();

  #if DEBUG
    Serial.print("Entries count: ");
    Serial.println(storage.readEntriesCount());
  #endif
  
  startBluetooth();

  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);
  
  // The scale value is the adc value corresponding to 1g
  scale.set_scale(27.61f);
  scale.tare();

  // Turn red LED
  M5.dis.drawpix(0, 0xFF0000); // TODO not working on start!
  M5.update();
}

void loop() {
  if (M5.Btn.pressedFor(3000)) {
    storage.resetMemory();
    #if DEBUG
      Serial.println("Memory was reset");
    #endif
    delay(1000);
  }
  
  if (M5.Btn.wasPressed()) {
    M5.dis.drawpix(0, 0x00FF00);
    uint16_t count = storage.readEntriesCount();
    storage.addEntry((count + 1) * 2);
    
    #if DEBUG
      storage.dumpMemory();
    #endif
    
    delay(50);
    M5.dis.drawpix(0, 0xFF0000);
  }
  
  if (deviceConnected) {
    int weight = scale.get_units(SCALE_TIMES_AVERAGE);
    
    if (weight > 0 && compare_floats(weight, prevWeight, COMPARE_DELTA) != 0) {
      // Notify with Blue that weight was changed
      M5.dis.drawpix(0, 0x00FF00);
      Serial.printf("New weight: %.2fkg \r\n", weight);
      writeData(String(weight));
      prevWeight = weight;
    }

    delay(500);
    
    // Notify with Green that device is connected
    M5.dis.drawpix(0, 0x00FF00);
    delay(THREAD_DELAY);
  } else {
    // Notify with Red color when device is disconnected
    M5.dis.drawpix(0, 0xFF0000);
    delay(50);
  }
  
  M5.update();
}

int compare_floats(float a, float b, float delta) {
  if (fabs(a - b) < delta) {
    return 0;
  } else if (a < b) {
    return -1;  // a is less than b
  } else {
    return 1;  // a is greater than b
  }
}
