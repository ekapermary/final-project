@signup
Feature: Sign up web Demoblaze

  Scenario: sign up akun Demoblaze
    Given user membuka halaman Demoblaze
    When user klik tombol sign up
    And user memasukkan username "<username>"
    And user memasukkan password "<password>"
    And user klik tombol sign up
    Then sistem akan menampilkan pesan "<expected_message>"

    Examples:
        | username     | password | expected_message          |
        | ekapermary   | welcome  | Sign up Successful        |
        | Admin356     | password | This user already exists  |
        | emptyuser    |          | Please fill out Username. |
        |              | welcome  | Please fill out Username. |

