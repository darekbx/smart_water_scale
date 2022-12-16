#include "storage.h"

void Storage::init() {
  // Initialize EEPROM memory to store data
  // 2    - address location
  // 2    - data count
  // 1024 - entries count
  EEPROM.begin(2 + 2 + 1024 * 2);
}

void Storage::resetMemory() {
  saveUShort(LAST_VALUE_ADDRESS, 0);
  saveUShort(ENTRIES_COUNT_ADDRESS, 0);
}

void Storage::addEntry(uint16_t value) {
  uint16_t address = readLastAddress();
  
  if (address == 0) {
    // No entries, initialize address
    address = ENTRIES_ADDRESS;
  } else {
    // Has entries, move address
    address += sizeof(unsigned short);
  }
  
  saveUShort(address, value);
  
  // Save new address
  saveUShort(LAST_VALUE_ADDRESS, address);
  
  increaseEntriesCount();
}

void Storage::loadEntries(uint16_t* output) {
  uint16_t count = readEntriesCount() * 2;
  for (uint16_t address = ENTRIES_ADDRESS, i = 0; address < count + ENTRIES_ADDRESS; address += 2, i++) { 
    output[i] = readUShort(address);
  }
}

uint16_t Storage::readEntriesCount() {
  return readUShort(ENTRIES_COUNT_ADDRESS);
}

void Storage::dumpMemory() {
  uint16_t count = readEntriesCount();
  uint16_t entries[count];
  
  loadEntries(entries);
  
  Serial.print("Entries count: ");
  Serial.println(count);

  Serial.print("Entries: ");
  for (uint16_t i = 0; i < count; i++) {
    if (i > 0) {
      Serial.print(", ");
    }
    Serial.print(entries[i]);
  }
  
  Serial.println();
}

/// Private
uint16_t Storage::readLastAddress() {
  return readUShort(LAST_VALUE_ADDRESS);
}

void Storage::increaseEntriesCount() {
  uint16_t count = readEntriesCount();
  saveUShort(ENTRIES_COUNT_ADDRESS, count + 1);
}

void Storage::saveUShort(uint16_t address, uint16_t value) {
  EEPROM.writeUShort(address, value);
  EEPROM.commit();
}

uint16_t Storage::readUShort(uint16_t address) {
  return EEPROM.readUShort(address);
}
