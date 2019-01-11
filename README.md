Cadastro de clientes

URL`s padroes
Cadastro: Post - localhost:8080/cliente POST para cadastro de cliente com os parametros: { "nome":"Exemplo", "idade":31  }
Lista Todos: GET - localhost:8080/cliente/all
List por id: GET - localhost:8080/cliente/ID
Edicao: PUT - localhost:8080/cliente com os parametros a serem alterados: { "id":1, "nome":"Teste", "idade":24 }
Remocao - DELETE - localhost:8080/cliente/ID

Tecnologias Utilizadas:
- Spring boot
- Spring Data
- Spring Rest
- banco H2

Testes
- Postman para simular as chamadas dos servicoes REST
