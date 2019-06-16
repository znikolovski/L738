This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

Below you will find some information on how to perform common tasks.<br>
You can find the most recent version of this guide [here](https://github.com/facebook/create-react-app/blob/master/packages/react-scripts/template/README.md).

## Table of Contents

- [Available Scripts](#available-scripts)
  - [npm start](#npm-start)
  - [npm run build](#npm-run-build)
  - [npm run styleguide](#npm-run-style-guide)
  

## Available Scripts

In the project directory, you can run:

### `npm start`

There are two possible ways to run the app in development mode. Both are commented in `.env.development` for reference.

**Proxy** the JSON model from AEM:

1. Start the development server:

    ```
    $ REACT_APP_PAGE_MODEL_PATH=/content/wknd-events/react.model.json npm start
    ```

2. Open a browser window and login to AEM at http://localhost:4502
3. Open a new tab in the same browser and navigate to [http://localhost:3000/content/wknd-events/react/home.html](http://localhost:3000/content/wknd-events/react/home.html)

**Mock** the JSON model locally:

1. Start the development server:

    ```
    $  REACT_APP_PAGE_MODEL_PATH=mock.model.json npm start
    ```
2. Open the browser and navigate to [http://localhost:3000/content/wknd-events/react/home.html](http://localhost:3000/content/wknd-events/react/home.html)

In both setups the page will reload if you make edits.<br>
You will also see any lint errors in the console.

### `npm run build`

Builds the app for production to the `build` folder.<br>
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br>
Your app is ready to be deployed!

### SSR Setup

The React App is already set up to make use of Server Side Rendering. In order to use Server Side Rendering you will need to perform the following steps:

1. Install the entire project from source code, from the root directory: `mvn -PautoInstallPackage clean install`
2. From the `react-app` directory run the following command:

    ```
    npm run start:server
    ```

