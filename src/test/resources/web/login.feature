@login

  feature: login ke website Demoblaze
  @positeve-Login
    scenario: User berhasil login ke website
        Given user membuka halaman utama Demoblaze
        When user klik Log in
        And user input "ekapermary" sebagai username
        And user input "welcome" sebagai password
        And user klik tombol Log in
        Then user akan diarahkan ke halaman utama

  @negatif-Login
    scenario: User gagal login ke website (salah password)
        Given user membuka halaman utama Demoblaze
        When user klik Log in
        And user input "ekapermary" sebagai username
        And user input "12345" sebagai password
        And user klik tombol Log in
        Then sistem menampilkan pesan "Wrong password."

    scenario: User gagal login ke website (username tidak terdaftar)
        Given user membuka halaman utama Demoblaze
        When user klik Log in
        And user input "test123" sebagai username
        And user input "12345" sebagai password
        And user klik tombol Log in
        Then sistem menampilkan pesan "User does not exist."