@checkout
Feature: Checkout pada website demoblaze

  @Positive
  Scenario Outline: Berhasil checkout produk di demoblaze
    Given user membuka halaman Demoblaze
    When user memilih produk "<produk>"
    And user menambahkan produk ke keranjang
    And user membuka halaman cart
    And user membuka halaman checkout
    And user melengkapi form checkout dengan data berikut:
      | name   | country   | city   | card   | month   | year   |
      | <name> | <country> | <city> | <card> | <month> | <year> |
    And user mengklik tombol Purchase
    Then sistem menampilkan pesan sukses dengan teks "Thank you for your purchase!"
    And user menutup notifikasi pop up

    Examples:
      | produk            | name  | country   | city    | card      | month | year |
      | Samsung galaxy s6 | Della | Indonesia | Bandung | 123456789 | 12    | 2025 |
      | Sony xperia z5    | Bimo  | Jepang    | Tokyo   | 987654321 | 05    | 2027 |

  @Negative
  Scenario Outline: User gagal checkout produk di demoblaze dengan data tidak valid
    Given user membuka halaman Demoblaze
    When user memilih produk "Sony xperia z5"
    And user menambahkan produk ke keranjang
    And user membuka halaman cart
    And user membuka halaman checkout
    And user melengkapi form checkout dengan data berikut:
      | name   | country   | city   | card   | month   | year   |
      | <name> | <country> | <city> | <card> | <month> | <year> |
    And user mengklik tombol Purchase dengan data validitas
    Then sistem menampilkan pesan "<expected_message>" sesuai hasil <isSuccess>

    Examples:
      | name | country   | city    | card      | month | year | isSuccess | expected_message                     |
      |      | Indonesia | Jakarta | 123456789 | 3     | 2024 | false     | Please fill out Name and Creditcard. |
      | Eka  | Indonesia | Jakarta |           | 3     | 2024 | false     | Please fill out Name and Creditcard. |
      | Eka  | Indonesia | Jakarta | abcd1234  | 3     | 2024 | true      | Thank you for your purchase!         |
      | Eka  | Indonesia | Jakarta | 1234abcd  | 3     | 2025 | true      | Thank you for your purchase!         |
      | Eka  | Indonesia | Jakarta | abcdefghi | 3     | 2025 | true      | Thank you for your purchase!         |
      | Eka  | Indonesia | Jakarta | 123456789 | 3     | 2025 | true      | Thank you for your purchase!         |
