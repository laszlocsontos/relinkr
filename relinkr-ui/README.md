# reLinkR UI

## Project setup
```
% npm install
```

### Compiles and hot-reloads for development
```
% npm run serve
```

### Run your tests
```
% npm run test
```

### Lints and fixes files
```
% npm run lint
```

### Run your unit tests
```
% npm run test:unit
```

### Compiles and minifies for production
```
% npm run build
```
### Compiles and minifies for staging

```
% npm run build -- --mode staging
```

## Deployment

1. Setup base path for the app

If you wish to use a non-root directory for the front-end, in that case Vue router must know the
path prefix to perform navigation properly. Environment variable `VUE_APP_UI_BASE_PATH` makes that
happen.

Create a file `.env.staging.local` or `.env.production.local` depending on which environment are you
building for and add the following content.

```
VUE_APP_UI_BASE_PATH=/[BUCKET_NAME]
```

2. Setup API base URL

A default value for `VUE_APP_API_BASE_URL` is set for both `staging` and `production` and you'll
have to change that for your own deployment. The procedure is the same as above, that is, add the
custom value to `.env.staging.local` or `.env.production.local`.

3. Compile and minify for `staging` or `production`

```
% npm run build -- --mode staging
% npm run build
```

4. Upload front-end

TODO

5. Setting up a custom domain.

TODO

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
