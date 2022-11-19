# reagent-boilerplate

Clojurescript reagent boilerplate: reagent, devcards, happy-dom & testing-library.  
_(created first with [create-cljs-app](https://github.com/filipesilva/create-cljs-app))_

## Directory structure
```
./reagent-boilerplate
├── cards              cards source code
|  ├── cards_node.cljs node test entry point
|  ├── cards_ui.cljs   cards ui entry point
|  ├── deck            unit cards
|  └── flow            flow cards
|
├── dev                dev source code
|  ├── dev_core.cljs   starts application with dev instrumentation
|  └── mock            dev mocking code
|
├── resources          application resources
|  ├── cards           public cards resouces
|  └── public          public application resources
|
└── src                production source code
   └── app             application source code
```
## Getting started
Run `yarn` to install dependencies and `yarn start` to start your application.  
| Script     | Description                                             |
|------------|---------------------------------------------------------|
| start      | Start application in watch mode ⇾ http://localhost:3000 |
| cards      | Start devcards UI ⇾ http://localhost:3001               |
| build      | Build application to `/resources/public`                |
| test       | Run tests in nodejs                                     |
| test:watch | Continuously run tests in nodejs (watch mode)           |
| report     | Creates a bundle size report                            |
| lint       | Lint source code                                        |
| format     | Format source code using zprint                         |
| clean      | Clean build artefacts                                   |

## License
This is free and unencumbered software released into the public domain.  
For more information, please refer to <http://unlicense.org>
