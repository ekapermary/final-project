@api

  feature: API Testing untuk DummyAPI.io

  @api
  scenario : create user
      Given saya memiliki user dengan detail firstName "<eka>", lastName "<permatasari>", and a unique email "<email>"
      When saya mengirimkan request untuk membuat user
      Then menampilkan response dengan status created successfully

  @api
  scenario : update detail by ID
      Given saya memiliki ID user
      When saya mengirim request GET untuk "/users/<id>"
      Then saya akan menerima response dengan status be 200 OK
      And saya akan mendapatkan detail user

  @api
  scenario : update detail user
      Given saya memiliki ID user
      And saya melakukan update dengan detail name "Della Update", dan email "della.update@example.com"
      When saya mengirim request PUT untuk "/users/<id>"
      Then saya akan menerima response dengan status be 200 OK
      And saya akan mendapatkan detail user yang telah diperbarui

  @api
  scenario : delete user
      Given saya memiliki ID user
      When saya mengirim request DELETE untuk "/users/<id>"
      Then saya akan menerima response dengan status be 200 OK
      And user tidak lagi ditemukan

