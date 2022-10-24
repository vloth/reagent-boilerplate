# reagent-boilerplate

Clojurescript reagent boilerplate: reagent, devcards, happy-dom & testing-library.  
_(created first with [create-cljs-app](https://github.com/filipesilva/create-cljs-app))_

## Directory structure
```
reagent-boilerplate
├── resources
|  ├── cards   cards public folder
|  └── public  application public folder
└── src
   ├── app     application source code
   ├── cards   cards and test source code
   └── dev     development and instrumentation
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
