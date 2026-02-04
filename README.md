# Management Equipment System — Java (Console)

## Descrição

Aplicação Java desenvolvida em ambiente console, com foco em Programação Orientada a Objetos, controle de estado de equipamentos de rede e persistência de dados em arquivo CSV.
O sistema simula o gerenciamento de equipamentos de infraestrutura (roteadores, switches, servidores e firewalls), permitindo operações de cadastro, controle operacional, cálculo de consumo de energia, geração de relatórios e registro de erros.

---

## Objetivos do Projeto

- Consolidar conceitos de POO em Java
- Trabalhar com coleções (List, Map)
- Implementar leitura e escrita de arquivos CSV
- Separar responsabilidades entre domínio, serviços e persistência
- Simular um sistema real de gerenciamento via linha de comando

---
### Menu do Sistema

Ao iniciar a aplicação, o usuário interage com o seguinte menu no console:

```text
Loading the equipment list file...
16 lines were loaded.

==============================
MANAGEMENT EQUIPMENT SYSTEM
==============================
1 - Register equipment
2 - List equipments
3 - Turn On / Turn Off / Restart Equipment
4 - Calculate Energy Consumption
5 - State Report
6 - Search Equipment by IP
7 - Remove Equipment by IP
8 - Generic Summary Report
9 - List error log
0 - Exit

Choose an option:
```
## Funcionalidades

- Registro de novos equipamentos
- Listagem de equipamentos cadastrados
- Controle de estado (ligar, desligar e reiniciar)
- Cálculo de consumo diário de energia
- Relatório de estado dos equipamentos
- Busca de equipamento por endereço IP
- Remoção de equipamento por IP
- Geração de relatório geral resumido
- Registro e listagem de logs de erro
- Carregamento automático dos dados ao iniciar o sistema
- Salvamento automático dos dados ao encerrar a aplicação

---

## Persistência de Dados

Os dados dos equipamentos são armazenados em um arquivo CSV externo:

```
equipments.csv

```

**Funcionamento:**

- Na inicialização, o sistema carrega automaticamente os equipamentos a partir do arquivo CSV
- Durante a execução, as operações são feitas em memória
- Ao encerrar o sistema, os dados atualizados são salvos novamente no arquivo CSV

> A persistência em CSV foi adotada para simplificar o projeto e evitar dependência de banco de dados, sendo adequada ao contexto de uma aplicação de console.
> 

---

## Exemplo de Arquivo CSV

```
ROUTER;Huawei WiFi BE3;192.168.0.10;Huawei;ON;20.0;24;true;1200
SWITCH;Cisco Catalyst2960;192.168.0.20;Cisco;ON;45.0;12;48.0
SERVER;Dell PowerEdge R740;192.168.0.5;Dell;ON;500.0;24;Linux;1024;2048
FIREWALL;FortiGate100E;192.168.0.30;Fortinet;ON;70.0;24;true;true

```

O sistema suporta diferentes tipos de equipamentos, cada um com atributos específicos, respeitando o modelo orientado a objetos.

---

## Estrutura do Projeto

- **Classes de domínio** representando os tipos de equipamentos
- **Serviços** responsáveis pelas regras de negócio
- **Camada de persistência** baseada em arquivo CSV
- **Menu interativo** para navegação e execução das funcionalidades
- **Logs de erro** para rastreabilidade de falhas

---

## Tecnologias Utilizadas

- Java
- Programação Orientada a Objetos
- Collections (List, Map)
- Manipulação de arquivos (CSV)
- Git / GitHub

---

## Como Executar

1. Clonar o repositório
2. Importar o projeto em uma IDE Java (Eclipse ou IntelliJ)
3. Garantir que o arquivo `equipments.csv` esteja disponível no diretório configurado
4. Executar a classe principal
5. Interagir com o sistema via menu no console

---

## Aprendizados

- Modelagem de classes e herança
- Encapsulamento e polimorfismo
- Controle de fluxo e menus interativos
- Persistência simples sem banco de dados
- Tratamento de erros e geração de logs
- Organização de código em camadas lógicas

---

##  Possíveis Evoluções

- Parametrização do caminho do arquivo CSV
- Validações mais avançadas de entrada
- Migração da persistência para banco de dados
- Interface gráfica ou API REST

---
