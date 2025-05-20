# Projeto 12 - Uber Report ⚠️
---
## 🏫 FECAP - Fundação de Comércio Álvares Penteado

<p align="center">
<a href= "https://www.fecap.br/"><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhZPrRa89Kma0ZZogxm0pi-tCn_TLKeHGVxywp-LXAFGR3B1DPouAJYHgKZGV0XTEf4AE&usqp=CAU" alt="FECAP - Fundação de Comércio Álvares Penteado" border="25.0px"></a>
</p>

---

## 👨‍💻 Integrantes: [André dos Santos](https://www.linkedin.com/in/andr%C3%A9-dos-santos-greg%C3%B3rio-025a402ba/), [Guilherme Fogolin](https://www.linkedin.com/in/guilhermefogolin/), [Pedro Lemos](https://www.linkedin.com/in/pedrohnlemos/) e [Yan Cezareto](https://www.linkedin.com/in/yan-cezareto-792ba22b8/)

---

## 👨‍🏫 Professores Orientadores: [Katia Milani Lara Bossi](https://www.linkedin.com/in/katia-bossi/), [Marco Aurelio](https://github.com/fecaphub/Portfolio/blob/main), [Victor Rosetti](https://www.linkedin.com/in/victorbarq/) e [Vinicius Heltai](https://www.linkedin.com/in/vheltai/)

---

## 📄 Descrição

<p align="center">
  <img src="./img/icon.png" alt="Logo Uber Report" width="300">
</p>

Visando uma implementação no aplicativo da Uber, o projeto tem como objetivo garantir maior segurança aos usuários e motoristas do app. Dessa forma, será possível reportar alertas em tempo real, indicando áreas de riscos e, assim, evitando potenciais incidentes ao pedir uma corrida na Uber.

---

## 📋 Detalhes

🚗 Já pensou você estar se preparando para pedir uma corrida ou já está em uma e presencia um **acidente? Um assalto? Uma via impedida?** Como conseguir contribuir para que outras pessoas fiquem cientes dessas situações?

🚨 O Uber Report vem para contribuir com essas questões: usuários e motoristas podem **alertar riscos** nas áreas das corridas e esses avisos serão mostrados no mapa. Logo, podem garantir maior segurança e confiança aos outros parceiros. 

☔ Com o uso de **alertas em tempo real** será possível relatar enchentes, desabamentos, quedas de árvores, assaltos, confrontos com a polícia e entre outros. Portanto, deixando claro os locais a serem evitados ou que exigem maior cautela.

📨 Se conectando com o **WhatsApp**, estará disponível o envio de mensagens rápidas e já prontas para um contato de confiança. Assim, é liberado compartilhar a sua viagem ou mesmo comunicar que está em potencial perigo.

🔐 Todos os dados enviados ao **servidor (CodeSandbox)** e armazenados no **banco de dados (SQLite)** são criptografados com o **padrão AES-128**, garantindo robustez no processo de segurança da informação.

⏳ Para evitar informações falsas, os alertas têm um **tempo de expiração** definido. Atualmente, para apresentações e praticidade, está definido como um minuto. 

🗾 Com a **API do Google Maps** implementamos o mapa, traçar rotas entre local atual para local de destino, pesquisar endereços no mundo inteiro e mostrar os alertas aos usuários.

---

## 🗂️ Estrutura de pastas

```
├── 🗂️ documentos/
│   ├── 📁 Entrega01
│   │  └── 📂 Análise Descritiva de Dados/
│   │  └── 📂 Programação Orientada a Objetos e Estrutura de Dados/
│   ├── 📁 Entrega02
│   │  └── 📂 Análise Descritiva de Dados/
│   │  └── 📂 Programação Orientada a Objetos e Estrutura de Dados/
│   │  └── 📂 Projeto Interdisciplinar: Aplicativo Móvel/
│   ├── 📁 Entrega03
│   │  └── 📂 Análise Descritiva de Dados/
│   │  └── 📂 Programação Orientada a Objetos e Estrutura de Dados/
│   ├── 📁 Banco de Dados/
├── 🗂️ img/
├── 🗂️ src/
│   ├── 📁 Entrega01
│   │  └── 📂 frontend/
│   │  └── 📂 backend/
│   ├── 📁 Entrega02
│   │  └── 📂 frontend/
│   │  └── 📂 backend/
│   ├── 📁 Entrega03
│   │  └── 📂 frontend/
│   │  └── 📂 backend/
└── 📄 readme.md
```

README.MD: Arquivo que serve como guia e explicação geral sobre o projeto.

Além disso, há outras pastas com os devidos arquivos em cada período de entrega.

⛲ [src](./src): Pasta que contém arquivos do frontend e backend do Uber Report, divididos por entregas conforme [cronograma da FECAP](https://docs.google.com/spreadsheets/d/1XAU0Du1hr3-Ovd_fm97lalqogyW0x3dZgiF1NAt3IaI/edit?gid=1182897581#gid=1182897581).

🎲 [Banco de Dados](./documentos/Banco%20de%20Dados): Banco de dados usado no projeto.

📄 [documentos](./documentos): Devidos documentos do projeto e arquivos relacionados ao Banco de Dados, POO e Análise Descritiva de Dados.

📸 [img](./img): Reunião de imagens utilizadas no projeto.

---

## 🛠️ Tutoriais de instalação

Há três modos que recomendamos para efetuar a instalação do aplicativo.

🚨 **Pontos de atenção:** Utilizando qualquer um dos métodos seguintes não será possível efetuar login ou criar uma conta, pois o nosso projeto implementa o servidor gratuitamente no **CodeSandbox**. Com isso, não fica disponível 24h por dia, sendo necessário que os administradores do projeto permitam que ele esteja rodando. Além disso, nos tutoriais **02** e **03** o build não será completo, uma vez que são necessárias as **credenciais da API do Google Maps** e **chave de criptografia AES** que, por motivos de segurança, não estão publicamente postados.

📨 **Contato:** Caso se interesse pelo funcionamento completo do app, por favor, entre em contato com algum dos participantes através dos links do LinkedIn acima ou ainda através do nosso e-mail: `uber.report.fecap@gmail.com`

--- 

### 📱 01. Instalando diretamente o APK

1️⃣ Nesse formato você deverá fazer o download do arquivo `.APK` na pasta [Entrega03 do PI](./documentos/Entrega03/Projeto%20Interdisciplinar_Aplicativo%20Móvel). 

2️⃣ Estando em um celular Android, faça a instalação.

3️⃣ Provavelmente aparecerá algum alerta, pois não está sendo feito o download diretamente da Play Store.

4️⃣ Confie na instalação e aproveite o aplicativo.

---

### 🗂️ 02. Download pelo arquivo .ZIP

1️⃣ Esse passo é recomendado nos casos que apresentar erros com o build Gradle do projeto.

2️⃣ Faça o download do arquivo `.ZIP` na pasta [Entrega03 do PI](./documentos/Entrega03/Projeto%20Interdisciplinar_Aplicativo%20Móvel).

3️⃣ Descompecte ela e abra com o Android Studio.

3️⃣ A IDE reconherá automaticamente as dependências necessárias e fará o download delas até o projeto estar totalmente buildado.

---

### ⛓️ 03. Clonando o repositório

1️⃣ Assim como o tutorial anterior, ao clonar o repositório o Android Studio irá reconhecer as dependências nessárias para download.

2️⃣ Efetue o clone através do botão `CODE` com o seguinte comando:

```
git clone git@github.com:2025-1-NCC3/Projeto12.git
```
3️⃣ Certifique-se de ter configurado o emulador e dê o play para rodar o app.

---

## 📹 Demonstração

Para além das instalações locais, você pode conhecer o aplicativo melhor através do seguinte vídeo: [Demonstração Uber Report - YouTube](https://www.youtube.com/watch?v=nwUi1H5nG1Q).


## 🎨 Figma

🖌️ O design do projeto inicialmente foi desenvolvido no Figma. Logo, abaixo há o link para o **protótipo** (simulação do app finalizado) e o link para **todas as telas** que implementamos.

📲 Link para o [protótipo](https://www.figma.com/proto/gzVLnfMOuGmtoptiRN9vwm/Projeto3Semestre-Mobile?node-id=20-676&p=f&t=qdB3RaowYYSDwNsz-1&scaling=scale-down&content-scaling=fixed&page-id=0%3A1&starting-point-node-id=26%3A188&show-proto-sidebar=1).

🌐 Link para o [projeto completo](https://www.figma.com/design/gzVLnfMOuGmtoptiRN9vwm/Projeto3Semestre-Mobile?node-id=0-1&p=f&t=KQBJIWzWs6puk3QV-0).

---

## ⚙️ Stacks

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)
![CodeSandbox](https://img.shields.io/badge/CodeSandbox-151515?style=for-the-badge&logo=codesandbox&logoColor=white)
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)
![Google Maps API](https://img.shields.io/badge/Google%20Maps-API-4285F4?style=for-the-badge&logo=googlemaps&logoColor=white)

---

## 📋 Licença

Uber Report © 2025 by André Gregório dos Santos, Guilherme Reis Fogolin de Godoy, Pedro Henrique Nascimento Lemos, Yan Cezareto Ramos is licensed under CC BY-NC-ND 4.0 

---

## 🎓 Referências

01. [Uber US Safety Report 2017 - 2018](https://www.uber-assets.com/image/upload/v1575580686/Documents/Safety/UberUSSafetyReport_201718_FullReport.pdf)

02. [Developer Android](https://developer.android.com/develop?hl=pt-br)

03. [Java Documentation](https://docs.oracle.com/en/java/)

04. [Google Maps Platform Documentation](https://developers.google.com/maps/documentation?hl=pt-br)

05. [CodeSandbox Overview](https://developers.google.com/maps/documentation?hl=pt-br)

06. [Estatísticas de Trânsito - Detran SP](https://www.detran.sp.gov.br/wps/portal/portaldetran/detran/estatisticastransito/)

---
