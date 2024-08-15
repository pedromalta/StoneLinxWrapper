# StoneLinxWrapper

Biblioteca para expor a SDK da Linx da Stone para uso no React Native.

## Instalação inicial

A biblioteca foi desenvolvida para ser utilizada com um .aar no projeto React Native, mas ela própria utilizar as dependencias .aar da SDK da Linx/Stone.

Para isso nós importamos os .aar da Linx/Stone para um repositório local e adicionamos a dependencia no build.gradle do projeto. Há um script em publish.gradle.kts que faz isso automaticamente.

Devemos chamar o script de publicação das bibliotecas com o comando:

```bash
./gradlew -b publish.gradle.kts publish
```

Após esse passo sincronizamos o projeto com os arquivos gradle e a biblioteca estará pronta pra ser gerada o aar.

Isso pode ser feito selcionando o module "stonelinxwrapper" e cliando no menu "build > make module StoneLinx_Wrapper.stonelinxwrapper" no Android Studio.

O Android Studio gera o .aar no path "stonelinxwrapper/build/outputs/aar/stonelinxwrapper-debug.aar". 

Por default ele vai gerar o debug, para gerar a versao release é só alterar a build variant na IDE
 
