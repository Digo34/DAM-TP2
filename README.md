# Tutorial 2 - Weather App

**Curso:** Engenharia Informática e Multimédia

**Estudante:** Rodrigo Silva, A51743

**Data:** 12/04/2026

**Link do Repositório:** https://github.com/Digo34/DAM-TP2

---

## 1. Introdução

Este segundo trabalho (TP2) da unidade curricular de Desenvolvimento de Aplicações Móveis (DAM) baseia-se nos conhecimentos adquiridos no TP1. Desafia os estudantes com conceitos avançados de Kotlin e cenários mais complexos de desenvolvimento de aplicações Android, incluindo a obtenção de dados de APIs REST externas, o tratamento de orientações de ecrã e layouts, e a implementação de padrões de arquitetura limpa.

O trabalho está dividido em dois componentes principais:

| Projeto | Secção | Foco |
|---------|--------|------|
| Exercícios Kotlin | §1 | Funções de extensão, Genéricos, Lambdas, Sobrecarga de Operadores |
| Cool Weather App | §2 | App Android, APIs REST, Temas de UI, Recursos XML, Coordenadas GPS |

Os objetivos de aprendizagem são:

- Aprofundar o conhecimento das funcionalidades avançadas do Kotlin, como genéricos, sobrecarga de operadores e lambdas.
- Criar designs Android responsivos que se adaptem a diferentes orientações e tamanhos de ecrã.
- Obter e apresentar dados reais de APIs públicas.
- Obter e apresentar automaticamente coordenadas GPS numa aplicação Android.

---

## 2. Visão Geral do Sistema

### 2.1 Exercícios Kotlin

Um conjunto de exercícios Kotlin isolados concebidos para explorar funcionalidades avançadas da linguagem:

- **Exercício 1.1 (Processamento de Registos de Eventos):** Utiliza funções de extensão, funções de ordem superior (`processEvents`) e classes seladas (`Event`) para processar uma sequência de registos de autenticação e compras de utilizadores.
- **Exercício 1.2 (Genéricos):** Uma cache em memória genérica e de tipo seguro `Cache<K, V>`, com funcionalidades como genéricos multi-parâmetro, `getOrPut`, remoção de entradas e snapshots imutáveis.
- **Exercício 1.3 (Pipeline de Dados Configurável):** Um pipeline funcional de processamento de dados que regista funções como transformadores baseados em etapas para processar listas de strings.
- **Exercício 1.4 (Sobrecarga de Operadores):** Uma classe matemática de vetor 2D (`Vec2`) com operadores sobrecarregados (`+`, `-`, `*`, `compareTo`, `[]`) para funcionar como um tipo numérico de primeira classe.

### 2.2 Cool Weather App

Uma aplicação Android que fornece dados meteorológicos em tempo real para uma localização específica, utilizando a API REST Open-Meteo. Gere diferentes estados de UI com temas Claro/Escuro e modos retrato/paisagem. A aplicação inclui a deteção automática de coordenadas GPS ao iniciar, e adapta-se a ecrãs maiores através do qualificador `sw600dp`.

---

## 3. Arquitetura e Design

### 3.1 Estrutura do Projeto Kotlin

Cada exercício segue princípios de design Kotlin modular, empregando conceitos como classes seladas para avaliação exaustiva durante o mapeamento de eventos, e genéricos e funções de ordem superior para escrever lógica de negócio reutilizável e desacoplada de tipos concretos.

### 3.2 Arquitetura da Cool Weather App

- **Adaptabilidade de Layout:** Disponibiliza definições de layout únicas para retrato, paisagem (`layout-land`) e tablets (`sw600dp`). Os fundos e as restrições de layout ajustam-se dinamicamente consoante o dispositivo e orientação.
- **Gestão de Recursos:** Os recursos de texto estão completamente extraídos para ficheiros XML, com suporte para inglês e português. As cores e os temas personalizados determinam a aparência da UI com base nas respostas da API de dia/noite.
- **Coordenadas GPS:** Ao iniciar a aplicação, as coordenadas iniciais são obtidas através do GPS interno do dispositivo utilizando o `FusedLocationProviderClient`, recorrendo às coordenadas de Lisboa como alternativa caso o GPS não esteja disponível.

---

## 4. Implementação

### 4.1 Exercícios Kotlin

- **1.1:** As funções de extensão `filterByUser` e `totalSpent` manipulam coleções usando `filterIsInstance` e `sumOf`.
- **1.2:** A classe `Cache` gere snapshots de mapas imutáveis para que os consumidores não possam alterar acidentalmente as referências de memória.
- **1.3:** O `Pipeline` utiliza `addStage()` e blocos de função do tipo `(List<String>) -> List<String>` aplicados sequencialmente.
- **1.4:** As propriedades vetoriais genéricas (`magnitude()`, `dot()`, `normalized()`) são implementadas juntamente com sobrecargas específicas do Kotlin como `operator fun plus()`.

### 4.2 Cool Weather App

- **API Open-Meteo:** Um pedido de rede construído com `java.net.URL` obtém um payload JSON com informações de pressão ao nível do mar, temperatura, direção e velocidade do vento, bem como horários de nascer e pôr do sol.
- **Mapeamento de Dados com Enumerações:** O enum `WMOWeatherCode` avalia os códigos meteorológicos devolvidos pela API para carregar seletivamente os ícones drawable correspondentes, como céu limpo, nevoeiro, chuva, neve, entre outros.
- **Temas Dia/Noite:** A aplicação determina automaticamente se é dia ou noite com base nos horários de nascer e pôr do sol devolvidos pela API, aplicando o tema e fundo corretos. A troca de tema é feita através de `SharedPreferences` para garantir persistência entre reinícios de atividade.
- **Localização do Dispositivo:** Ao iniciar, as coordenadas são obtidas automaticamente através do GPS interno do dispositivo. Caso o utilizador negue a permissão ou o GPS não esteja disponível, a aplicação recorre às coordenadas predefinidas de Lisboa.

---

## 5. Testes e Validação

### 5.1 Exercícios Kotlin

- Testados manualmente através de funções `main()` em Kotlin, verificando que os resultados obtidos correspondiam aos resultados esperados conforme as instruções.
- Avaliadas colisões na cache e consultas fora dos limites definidos.
- Verificado que a aritmética vetorial produzia os resultados esperados em vírgula flutuante.

### 5.2 Cool Weather App

- Testados múltiplos formatos de dispositivo no emulador, para garantir que o `ConstraintLayout` e a lógica `sw600dp` funcionavam corretamente.
- Simuladas entradas de coordenadas inválidas e alterações de localização para verificar que o mapeamento JSON atualizava corretamente os ícones meteorológicos e os dados apresentados.
- Testada a mudança de tema dia/noite utilizando coordenadas de diferentes fusos horários, como Sydney e Tóquio para noite, e Nova Iorque e Lisboa para dia (de acordo com o horário da altura).
- Verificada a deteção automática de coordenadas GPS ao iniciar a aplicação, tanto com permissão concedida como negada.
- Testada a persistência das coordenadas e do tema entre reinícios de atividade provocados pela mudança de tema dia/noite.

---

## 6. Instruções de Utilização

### 6.1 Requisitos

- Android Studio Hedgehog ou superior
- Android SDK API 24 ou superior

### 6.2 Executar os Exercícios Kotlin

1. Abrir a pasta do projeto dos exercícios Kotlin no IntelliJ IDEA.
2. Executar a função `main()` do ficheiro de exercício que se pretende testar.

### 6.3 Executar a Cool Weather App

1. Abrir a pasta `CoolWeatherApp` como projeto no Android Studio.
2. Sincronizar os ficheiros Gradle.
3. Ligar a um AVD (Android Virtual Device) ou dispositivo físico e lançar a aplicação.
4. Aceitar a permissão de localização para que a aplicação detete automaticamente as coordenadas GPS do dispositivo.

---

## 7. Dificuldades e Lições Aprendidas

Durante o desenvolvimento da Cool Weather App, foram encontradas várias dificuldades:

- **Persistência do tema dia/noite entre reinícios de atividade:** A troca de tema entre dia e noite exige que o tema seja definido antes de `setContentView()`. Isto tornou complexa a persistência do valor `day` entre reinícios de atividade provocados por `recreate()`, uma vez que o valor era reposto antes de ser lido corretamente. A solução passou pela utilização de `SharedPreferences` com escrita síncrona (`commit = true`) para garantir que o valor estava disponível antes do reinício.

- **URL da API malformada:** Durante o desenvolvimento, foi detetado que a URL construída para a API Open-Meteo estava malformada por falta de um `&` entre os parâmetros `hourly` e `daily`, o que causava um erro `FileNotFoundException`. A solução passou por uma revisão cuidadosa da construção da string da URL.

- **Ciclo infinito de reinícios de atividade:** Ao chamar `recreate()` para aplicar um novo tema, a atividade voltava a chamar `fetchWeatherData()` com as coordenadas de Lisboa predefinidas, o que sobrescrevia o valor de `day` e causava um novo reinício. A solução passou pela introdução de uma flag `isRecreating` em `SharedPreferences` para distinguir um reinício normal de um reinício provocado pela mudança de tema.

- **Permissões de localização GPS:** A obtenção das coordenadas GPS do dispositivo exigiu a implementação de um fluxo de pedido de permissões em tempo de execução, tratando os casos em que o utilizador nega a permissão ou em que a última localização conhecida não está disponível no emulador.

---

## 8. Melhorias Futuras

Após a conclusão do desenvolvimento da Cool Weather App, foram identificadas as seguintes
melhorias prioritárias para versões futuras:

- **Padrão de arquitetura MVVM:** Refatorar o código da `MainActivity` para seguir o padrão Model-View-ViewModel, separando a lógica de negócio da interface gráfica. Isto tornaria o código mais organizado, testável e escalável, utilizando `ViewModel`, `LiveData` e um `Repository` para gerir os dados da API.

- **Recursos XML para códigos meteorológicos:** Em vez de utilizar um enum estático para mapear os códigos WMO, carregar essa informação dinamicamente a partir de um ficheiro de recursos XML. Isto tornaria o código mais flexível e mais fácil de manter, sem necessidade de recompilar a aplicação para adicionar novos códigos.

- **Tratamento de erros de rede:** Adicionar tratamento de erros adequado para situações como ausência de ligação à internet, timeout da API, ou coordenadas inválidas. Seria útil apresentar uma mensagem de erro ao utilizador e permitir que tente novamente.

- **Indicador de carregamento:** Apresentar um indicador visual de carregamento enquanto os dados meteorológicos estão a ser obtidos da API, melhorando a experiência do utilizador ao tornar claro que a aplicação está a processar informação.

---

## 9. Declaração de Utilização de IA (Obrigatório)

Durante a realização deste trabalho, foram utilizadas ferramentas de IA como apoio, nomeadamente para a identificação e correção de erros no código, e para a resolução de dúvidas específicas que surgiram ao longo do processo.

Nenhuma ferramenta de geração automática de código foi utilizada diretamente para produzir a solução final do trabalho. O código apresentado foi desenvolvido manualmente com base na documentação oficial do Kotlin e do Android Studio, bem como em recursos técnicos disponíveis online.

As ferramentas de IA foram utilizadas exclusivamente como suporte de aprendizagem, resolução de problemas pontuais e escrita deste mesmo relatório, e não para gerar automaticamente a solução completa dos exercícios ou da aplicação.
