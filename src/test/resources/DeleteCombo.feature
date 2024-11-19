Feature: Delete Combo (p3)
  As a manager, I want to delete existing combos so that I can restrict the combos available to participants

  Background: 
    Given the following BikeTourPlus system exists: (p3)
      | startDate  | nrWeeks | priceOfGuidePerWeek |
      | 2023-03-13 |      10 |                 100 |
    Given the following pieces of gear exist in the system: (p3)
      | name     | pricePerWeek |
      | helmet   |           25 |
      | e-bike   |          150 |
      | bike bag |           19 |
    Given the following combos exist in the system: (p3)
      | name        | discount | items                  | quantity |
      | small combo |       10 | e-bike,helmet          |      1,2 |
      | large combo |       25 | e-bike,helmet,bike bag |    1,2,2 |

  Scenario: Delete a combo successfully
    When the manager attempts to delete the combo with name "<name>" (p3)
    Then a combo shall not exist with name "<name>" (p3)
    Then the number of combos shall be "<numberOfCombos>" (p3)

    Examples: 
      | name         | numberOfCombos |
      | small combo  |              1 |
      | large combo  |              1 |
      | deluxe combo |              2 |
      | combo plus   |              2 |

  Scenario: Successfully delete a combo that has been requested by a participant
    Given the following participants exist in the system: (p3)
      | email           | password | name  | emergencyContact | nrWeeks | weeksAvailableFrom | weeksAvailableUntil | lodgeRequired |
      | peter@email.com | pass1    | Peter | (666)555-5555    |       1 |                 1  |                  2  | true          |
    Given the following participants request the following combos: (p3)
      | email           | combo       | quantity |
      | peter@email.com | small combo |        1 |
      | peter@email.com | large combo |        2 |
    When the manager attempts to delete the combo with name "<name>" (p3)
    Then a combo shall not exist with name "<name>" (p3)
    Then the number of combos shall be "1" (p3)
    Then the participant with email "<email>" shall have a combo with name "<requestedComboName>" and quantity "<quantity>" (p3)
    Then the number of pieces of gear for the participant with email "<email>" shall be "<numberOfRequestedCombos>" (p3)

    Examples: 
      | name          | email           | requestedComboName | quantity | numberOfRequestedCombos |
      | small combo   | peter@email.com | large combo        |        2 |                       1 |
      | large combo   | peter@email.com | small combo        |        1 |                       1 |
