@signup
Feature: Sign up web Demoblaze

  Scenario Outline: sign up akun Demoblaze
    Given user membuka halaman Demoblaze
    When user klik sign up
    And user memasukkan username "<username>"
    And user memasukkan password "<password>"
    And user click tombol sign up
    Then sistem akan menampilkan pesan "<expectedMessage>"

    Examples:
        | username     | password | expectedMessage          |
        | ekapermary   | welcome  | Sign up Successful        |
        | Admin356     | password | This user already exist  |
#       | emptyuser    |          | Please fill out Username. |
#       |              | welcome  | Please fill out Username. |

