# Desafio Senior

# Ambiente

### Configurações
Configurações de base e de porta podem ser alteradas nos arquivos:

Por padrão a aplicação roda em uma base POSTGRESQL 
- ```/desafio-senior/src/main/resources/application.properties```

Por padrão os testes rodam em uma base H2
 - ```/desafio-senior/src/test/java/test.properties```

### Aplicações

Classe para rodar a aplicação spring-boot:
```
/desafio-senior/src/main/java/br/com/luzrafaelf/desafio/DesafioApplication.java
```

Classe para rodar os testes:
```
/desafio-senior/src/test/java/br/com/luzrafaelf/desafio/test/Tests.java
```

# API

## Response
O corpo de resposta padrão de erros irá trazer a formatação:
```JSON
{
  "error": "Descrição do problema"
}
```

## Produtos

### Listar
[GET] ```/rest/produtos```
Irá listar todos os produtos cadastrados

Parâmetros:
| Parâmetro      | Requirido     | Formato     | Descrição                                       |
|----------------|---------------|-------------|--------------------------------
| $page          | Sim           | int         | Número da página                                
| $quantity      | Sim           | int         | Quantidade de itens por página                
| Tipo           | Não           | String      | Irá trazer os produtos do tipo (PRODUTO ou SERVICO)
| Ativo          | Não           | Boolean     | Irá trazer os produtos ativos ou inativos (true ou false)

### Ler
[GET] ```/rest/produtos/{id do produto}```
Irá trazer um produto cadastrado

### Criar
[POST] ```/rest/produtos```
Irá criar um produto

Exemplo de corpo de request:
```JSON
{
	"ativo": false,
	"tipo": "PRODUTO",
	"valorCusto": 100.00
}
```

### Atualizar
[PUT] ```/rest/produtos/{id do produto}```
Irá atualizar os dados de um produto já cadastrado

Exemplo de corpo de request:
```JSON
{
	"ativo": false,
	"tipo": "PRODUTO",
	"valorCusto": 100.00
}
```

### Remover
[DELETE] ```/rest/produtos/{id do produto}```
Irá deletar um produto cadastrado
- O produto não pode fazer parte de um pedido

## Pedidos

### Listar
[GET] ```/rest/pedidos```
Irá listar todos os pedidos cadastrados

Parâmetros:
| Parâmetro      | Requirido     | Formato     | Descrição                                       |
|----------------|---------------|-------------|---------------------------------------------
| $page          | Sim           | int         | Número da página                               
| $quantity      | Sim           | int         | Quantidade de itens por página                  
| data-inicial   | Não           | yyyy-MM-dd  | Irá trazer todos os pedidos com a data igual ou superior à informada
| data-final     | Não           | yyyy-MM-dd  | Irá trazer todos os pedidos com a data igual ou inferior à informada       
| situacao       | Não           | String      | Irá trazer os pedidos com essa situação (ABERTO ou FECHADO)      

### Ler
[GET] ```/rest/pedidos/{id do pedido}```
Irá trazer um produto cadastrado

### Criar
[POST] ```/rest/pedidos/```
Irá criar um pedido

Exemplo de corpo de request:
```JSON
{
	"situacao" : "FECHADO",
	"data" : "2020-01-09"
}
```

### Atualizar
[PUT] ```/rest/pedidos/```
Irá atualizar os dados do pedido

Exemplo de corpo de request:
```JSON
{
	"situacao" : "ABERTO",
	"data" : "2021-06-09"
}
```

### Remover
[DELETE] ```/rest/pedidos/{id do pedido}```
Remove o pedido e todos os seus itens

### Atribuir desconto ao pedido
[PUT] ```/rest/pedidos/{id do pedido}/desconto```
Irá atribuir um percentual de desconto para o pedido
- O valor do percentual precisa estar entre 0 e 100

Exemplo de corpo de request:
```JSON
{
	"percentualDesconto": 15
}
```

## Itens do Pedido

### Adicionar item ao pedido
[PUT] ```/rest/pedidos/{id do pedido}/adicionar-item```
Irá criar o item no pedido
- Isso irá recalcular o "valorTotal" do pedido
- Somente pode adicionar o item se o produto estiver ativo

Exemplo de corpo de request:
```JSON
{
	"produto":
	{
		"id": "3562bf2f-5e7d-444c-a3a9-ba6d208acc6f"
	}
}
```
### Atualizar item do pedido
[PUT] ```/rest/pedidos/{id do pedido}/adicionar-item```
Irá atualizar o item no pedido
- Isso irá recalcular o "valorTotal" do pedido
- 
Exemplo de corpo de request:
```JSON
{
	"produto":
	{
		"id": "3562bf2f-5e7d-444c-a3a9-ba6d208acc6f"
	}
}
```

### Remover item do pedido
[PUT] ```/rest/pedidos/{id do pedido}/remover-item```
Irá remover o item do pedido
- Isso irá recalcular o "valorTotal" do pedido

Exemplo de corpo de request:
```JSON
{
	"id": "72f64801-0a55-4d25-a2cb-1373191c3150"
}
```
