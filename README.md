# cards

O projeto segue os requisitos descritos neste link: https://bitbucket.org/petzdigital/teste-tecnico-mobile/src/main/

Os parametros a serem exibidos foram considerados de maneira literal, e nos resultados da API como muitos dos elementos da lista as vezes não continham nem metade
dos parametros cuja exibição era solicitada, foi criado um filtro para eliminar um elemento sempre que qualquer um dos parametros fosse null. Também foram filtrados os
elementos duplicados. Uma solução alternativa para isso seria flexibilizar os requisitos e mostrar algo como uma mensagem "não se aplica" para os campos não retornados
pela API. A lista prévia dos elementos já contem algumas informações principais, como flavor, nome e imagem. 

Considerações técnicas de elementos utilizados e possíveis adições e modificações:

 1 - Para fazer a filtragem de parametros no dataclass, foi utilizado reflection, que nao é uma boa prática por questões de desacoplamento e outros. A solução alternativa
 para isso já foi fornecida na introdução deste README. 
 
 2 - Foi utilizada a função notifySetDataChanged para atualizar o Adapter, o que não é recomendado considerando o custo dessa operação. Entretanto, como o nosso unico caso
 de uso é a atualização de todo o dataSet, essa função foi mantida. Uma solução alternativa para isso, seria usar um DiffUtil ou uma operação mais especifica, como por 
 exemplo notifyItemChanged. 
 
 3 - O clicklistener dos itens no recyclerview também poderia ser feito de maneira mais generalista, no createViewHolder. A solução atual foi escolhida por ser mais rápida
 de implementar e porque o impacto na nossa aplicação é reduzido dado o seu tamanho. 
 
 4 - Uma solução melhor envolveria um fragmento para a tela de detalhes, utilizando-se do componente de navegação do jetpack. O mesmo não foi implementado pela mesma 
 causa do item 3.
 
 5 - Foram construídos testes unitários completos para a camada de viewModel da aplicação. Entretanto, o ideal seria adicionar também testes unitários para o nosso 
 CardsManager. 
 
 6 - A separação das camadas de data, domain e UseCase foram simplificadas, e deveriam estar melhor separadas para o caso de um projeto maior. 
 
 7 - O hilt foi selecionado como ferramenta de injeção de dependencia. 
