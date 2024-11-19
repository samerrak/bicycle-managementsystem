Feature: Register and Update Participant (p1)
  As a participant, I want to register and update my participant account so that I can take a bike tour

  Background:
    Given the following BikeTourPlus system exists: (p1)
      | startDate  | nrWeeks | priceOfGuidePerWeek |
      | 2023-03-13 |      10 |                 100 |
    Given the following guides exist in the system: (p1)
      | email          | password | name | emergencyContact |
      | jeff@email.com | pass1    | Jeff | (555)555-5555    |
      | john@email.com | pass2    | John | (444)444-4444    |
    Given the following participants exist in the system: (p1)
      | email           | password | name  | emergencyContact | nrWeeks | weeksAvailableFrom | weeksAvailableUntil | lodgeRequired |
      | peter@email.com | pass1    | Peter | (666)555-5555    |       1 |                  1 |                   2 | true          |
      | tyler@email.com | pass2    | Tyler | (777)444-4444    |       2 |                  2 |                   5 | false         |

  Scenario Outline: Register a participant successfully
    When a new participant attempts to register with email "<email>", password "<password>", name "<name>", emergency contact "<emergencyContact>", number of weeks "<nrWeeks>", week available from "<weeksAvailableFrom>", week available until "<weeksAvailableUntil>", and lodge required "<lodgeRequired>" (p1)
    Then a participant account shall exist with email "<email>", password "<password>", name "<name>", emergency contact "<emergencyContact>", number of weeks "<nrWeeks>", week available from "<weeksAvailableFrom>", week available until "<weeksAvailableUntil>", and lodge required "<lodgeRequired>" (p1)
    Then the number of participants shall be "3" (p1)

    Examples:
      | email         | password  | name  | emergencyContact | nrWeeks | weeksAvailableFrom | weeksAvailableUntil | lodgeRequired |
      | user3@mail.ca | password3 | User3 | (333)333-3333    |       3 |                  3 |                  10 | true          |
      | user4@mail.ca | password4 | User4 | (444)444-4444    |       4 |                  4 |                   7 | true          |

  Scenario Outline: Register a participant unsuccessfully
    When a new participant attempts to register with email "<email>", password "<password>", name "<name>", emergency contact "<emergencyContact>", number of weeks "<nrWeeks>", week available from "<weeksAvailableFrom>", week available until "<weeksAvailableUntil>", and lodge required "<lodgeRequired>" (p1)
    Then a participant account shall not exist with email "<email>", password "<password>", name "<name>", emergency contact "<emergencyContact>", number of weeks "<nrWeeks>", week available from "<weeksAvailableFrom>", week available until "<weeksAvailableUntil>", and lodge required "<lodgeRequired>" (p1)
    Then the number of participants shall be "2" (p1)
    Then the system shall raise the error "<error>" (p1)

    Examples:
      | email           | password | name | emergencyContact | nrWeeks | weeksAvailableFrom | weeksAvailableUntil | lodgeRequired | error                                                                                                |
      | manager@btp.com | password | User | (555)555-5555    |       5 |                  2 |                   6 | true          | Email cannot be manager@btp.com                                                                      |
      | john@email.com  | password | User | (111)111-1111    |       1 |                  1 |                  10 | true          | Email already linked to a guide account                                                              |
      | peter@email.com | password | User | (111)111-1111    |       1 |                  1 |                  10 | true          | Email already linked to a participant account                                                        |
      | user@ mail.ca   | password | User | (111)222-333     |       1 |                  8 |                   9 | true          | Email must not contain any spaces                                                                    |
      | user@mail@y.ca  | password | User | (111)222-333     |       1 |                  3 |                  10 | true          | Invalid email                                                                                        |
      | kyle@email.     | password | User | (111)222-333     |       1 |                  3 |                  10 | true          | Invalid email                                                                                        |
      | user@mail       | password | User | (111)222-333     |       1 |                  3 |                  10 | true          | Invalid email                                                                                        |
      | @mail.ca        | password | User | (111)222-333     |       1 |                  3 |                  10 | true          | Invalid email                                                                                        |
      | user@.ca        | password | User | (111)222-333     |       1 |                  3 |                  10 | true          | Invalid email                                                                                        |
      |                 | password | User | (111)222-333     |       5 |                  2 |                  10 | false         | Email cannot be empty                                                                                |
      | user@mail.ca    |          | User | (555)555-5555    |       5 |                  2 |                  10 | false         | Password cannot be empty                                                                             |
      | user@mail.ca    | password |      | (555)555-5555    |       5 |                  2 |                  10 | false         | Name cannot be empty                                                                                 |
      | user@mail.ca    | password | User |                  |       5 |                  2 |                  10 | false         | Emergency contact cannot be empty                                                                    |
      | user@mail.ca    | password | User | (555)555-5555    |       0 |                  5 |                   8 | true          | Number of weeks must be greater than zero                                                            |
      | user@mail.ca    | password | User | (555)555-5555    |      11 |                  5 |                   8 | true          | Number of weeks must be less than or equal to the number of biking weeks in the biking season        |
      | user@mail.ca    | password | User | (555)555-5555    |       3 |                  5 |                   6 | true          | Number of weeks must be less than or equal to the number of available weeks                          |
      | user@mail.ca    | password | User | (555)555-5555    |       3 |                  6 |                   5 | true          | Week from which one is available must be less than or equal to the week until which one is available |
      | user@mail.ca    | password | User | (555)555-5555    |       3 |                  0 |                   6 | true          | Available weeks must be within weeks of biking season (1-10)                                         |
      | user@mail.ca    | password | User | (555)555-5555    |       3 |                 11 |                   6 | true          | Available weeks must be within weeks of biking season (1-10)                                         |
      | user@mail.ca    | password | User | (555)555-5555    |       3 |                 -1 |                   0 | true          | Available weeks must be within weeks of biking season (1-10)                                         |
      | user@mail.ca    | password | User | (555)555-5555    |       3 |                  6 |                  11 | true          | Available weeks must be within weeks of biking season (1-10)                                         |

  Scenario Outline: Update a participant account successfully
    When the participant with email "<email>" attempts to update their account information to password "<newPassword>", name "<newName>", emergency contact "<newEmergencyContact>", number of weeks "<newNrWeeks>", week available from "<newWeeksAvailableFrom>", week available until "<newWeeksAvailableUntil>", and lodge required "<newLodgeRequired>" (p1)
    Then a participant account shall exist with email "<email>", password "<newPassword>", name "<newName>", emergency contact "<newEmergencyContact>", number of weeks "<newNrWeeks>", week available from "<newWeeksAvailableFrom>", week available until "<newWeeksAvailableUntil>", and lodge required "<newLodgeRequired>" (p1)
    Then a participant account shall not exist with email "<email>", password "<password>", name "<name>", emergency contact "<emergencyContact>", number of weeks "<nrWeeks>", week available from "<weeksAvailableFrom>", week available until "<weeksAvailableUntil>", and lodge required "<lodgeRequired>" (p1)
    Then the number of participants shall be "2" (p1)

    Examples:
      | email           | password | name  | emergencyContact | nrWeeks | weeksAvailableFrom | weeksAvailableUntil | lodgeRequired | newPassword  | newName  | newEmergencyContact | newNrWeeks | newWeeksAvailableFrom | newWeeksAvailableUntil | newLodgeRequired |
      | peter@email.com | pass1    | Peter | (666)555-5555    |       1 |                  1 |                   2 | true          | password3    | User3    | (333)333-3333       |          3 |                     3 |                     10 | true             |
      | tyler@email.com | pass2    | Tyler | (777)444-4444    |       2 |                  2 |                   5 | false         | password4    | User4    | (444)444-4444       |          4 |                     4 |                      7 | true             |

  Scenario Outline: Update a participant account unsuccessfully
    When the participant with email "<email>" attempts to update their account information to password "<newPassword>", name "<newName>", emergency contact "<newEmergencyContact>", number of weeks "<newNrWeeks>", week available from "<newWeeksAvailableFrom>", week available until "<newWeeksAvailableUntil>", and lodge required "<newLodgeRequired>" (p1)
    Then a participant account shall exist with email "<email>", password "<password>", name "<name>", emergency contact "<emergencyContact>", number of weeks "<nrWeeks>", week available from "<weeksAvailableFrom>", week available until "<weeksAvailableUntil>", and lodge required "<lodgeRequired>" (p1)
    Then a participant account shall not exist with email "<email>", password "<newPassword>", name "<newName>", emergency contact "<newEmergencyContact>", number of weeks "<newNrWeeks>", week available from "<newWeeksAvailableFrom>", week available until "<newWeeksAvailableUntil>", and lodge required "<newLodgeRequired>" (p1)
    Then the number of participants shall be "2" (p1)
    Then the system shall raise the error "<error>" (p1)

    Examples:
      | email           | password | name  | emergencyContact | nrWeeks | weeksAvailableFrom | weeksAvailableUntil | lodgeRequired | newPassword | newName | newEmergencyContact | newNrWeeks | newWeeksAvailableFrom | newWeeksAvailableUntil | newLodgeRequired | error                                                                                                |
      | peter@email.com | pass1    | Peter | (666)555-5555    |       1 |                  1 |                   2 | true          |             | User    | (555)555-5555       |          5 |                     2 |                     10 | false            | Password cannot be empty                                                                             |
      | peter@email.com | pass1    | Peter | (666)555-5555    |       1 |                  1 |                   2 | true          | password    |         | (555)555-5555       |          5 |                     2 |                     10 | false            | Name cannot be empty                                                                                 |
      | peter@email.com | pass1    | Peter | (666)555-5555    |       1 |                  1 |                   2 | true          | password    | User    |                     |          5 |                     2 |                     10 | false            | Emergency contact cannot be empty                                                                    |
      | peter@email.com | pass1    | Peter | (666)555-5555    |       1 |                  1 |                   2 | true          | password    | User    | (555)555-5555       |          0 |                     5 |                      8 | true             | Number of weeks must be greater than zero                                                            |
      | peter@email.com | pass1    | Peter | (666)555-5555    |       1 |                  1 |                   2 | true          | password    | User    | (555)555-5555       |         11 |                     5 |                      8 | true             | Number of weeks must be less than or equal to the number of biking weeks in the biking season        |
      | tyler@email.com | pass2    | Tyler | (777)444-4444    |       2 |                  2 |                   5 | false         | password    | User    | (555)555-5555       |          3 |                     5 |                      6 | true             | Number of weeks must be less than or equal to the number of available weeks                          |
      | tyler@email.com | pass2    | Tyler | (777)444-4444    |       2 |                  2 |                   5 | false         | password    | User    | (555)555-5555       |          3 |                     6 |                      5 | true             | Week from which one is available must be less than or equal to the week until which one is available |
      | tyler@email.com | pass2    | Tyler | (777)444-4444    |       2 |                  2 |                   5 | false         | password    | User    | (555)555-5555       |          3 |                     0 |                      6 | true             | Available weeks must be within weeks of biking season (1-10)                                         |
      | tyler@email.com | pass2    | Tyler | (777)444-4444    |       2 |                  2 |                   5 | false         | password    | User    | (555)555-5555       |          3 |                    11 |                      6 | true             | Available weeks must be within weeks of biking season (1-10)                                         |
      | tyler@email.com | pass2    | Tyler | (777)444-4444    |       2 |                  2 |                   5 | false         | password    | User    | (555)555-5555       |          3 |                    -1 |                      0 | true             | Available weeks must be within weeks of biking season (1-10)                                         |
      | tyler@email.com | pass2    | Tyler | (777)444-4444    |       2 |                  2 |                   5 | false         | password    | User    | (555)555-5555       |          3 |                     6 |                     11 | true             | Available weeks must be within weeks of biking season (1-10)                                         |

  Scenario Outline: Unsuccessfully update a participant account that does not exist
    When the participant with email "<email>" attempts to update their account information to password "<newPassword>", name "<newName>", emergency contact "<newEmergencyContact>", number of weeks "<newNrWeeks>", week available from "<newWeeksAvailableFrom>", week available until "<newWeeksAvailableUntil>", and lodge required "<newLodgeRequired>" (p1)
    Then a participant account shall not exist with email "<email>", password "<password>", name "<name>", emergency contact "<emergencyContact>", number of weeks "<nrWeeks>", week available from "<weeksAvailableFrom>", week available until "<weeksAvailableUntil>", and lodge required "<lodgeRequired>" (p1)
    Then a participant account shall not exist with email "<email>", password "<newPassword>", name "<newName>", emergency contact "<newEmergencyContact>", number of weeks "<newNrWeeks>", week available from "<newWeeksAvailableFrom>", week available until "<newWeeksAvailableUntil>", and lodge required "<newLodgeRequired>" (p1)
    Then the number of participants shall be "2" (p1)
    Then the system shall raise the error "<error>" (p1)

    Examples:
      | email          | password | name  | emergencyContact | nrWeeks | weeksAvailableFrom | weeksAvailableUntil | lodgeRequired | newPassword | newName | newEmergencyContact | newNrWeeks | newWeeksAvailableFrom | newWeeksAvailableUntil | newLodgeRequired | error                                  |
      | jane@email.com | pass1    | Peter | (666)555-5555    |       1 |                  1 |                   2 | true          |             | User    | (555)555-5555       |          5 |                     2 |                     10 | false            | The participant account does not exist |
