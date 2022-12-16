#include <EEPROM.h>

#define LAST_VALUE_ADDRESS    0
#define ENTRIES_COUNT_ADDRESS 2
#define ENTRIES_ADDRESS       4

/**
 * EEPROM
 * 
 * Values are stored as UShort (2^16 - 1 = 65535)
 * 
 * Addressing:
 * 0, 1 = address of the latest value
 * 2, 3 = count of the values
 * 4, ... = entries
 * 
 */
class Storage {

  public:
    void init();
    void resetMemory();
    void addEntry(uint16_t value);
    void loadEntries(uint16_t* output);
    uint16_t readEntriesCount();
    void dumpMemory();

  private:
    uint16_t readLastAddress();
    void increaseEntriesCount();
    void saveUShort(uint16_t address, uint16_t value);
    uint16_t readUShort(uint16_t address);
};
