# CI Scripts

## run_and_check.sh

This script automatically tests the play-pac4j-java-demo application by:

1. **Building the application**: Clean and compile using sbt
2. **Starting the server**: Launch the Play application on port 9000
3. **Basic HTTP tests**: Verify the app responds with HTTP 200
4. **CAS authentication flow**: Test the full CAS login process
   - Follow redirects to CAS login page
   - Extract execution token from the login form
   - Submit credentials (leleuj@gmail.com / leleuj)
   - Verify successful authentication and return to the app
   - Check that user profile is correctly displayed

### Usage

#### Local usage:
```bash
# From the ci/ directory
cd ci
./run_and_check.sh

# Or from the project root
ci/run_and_check.sh
```

#### CI/GitHub Actions:
The script is automatically executed by the GitHub Actions workflow in `.github/workflows/build-and-test.yml`.

### Requirements

- Java 17+
- sbt (automatically installed in CI, or downloaded as fallback)
- curl (for HTTP testing)
- Internet access (for CAS server communication)

### What it validates

✅ **Application startup**: Server starts without errors  
✅ **Basic functionality**: HTTP 200 response on homepage  
✅ **CAS integration**: Full authentication flow works  
✅ **Session management**: User profile is correctly stored and displayed  
✅ **Security features**: CSRF tokens and session handling work properly  

### Troubleshooting

If the script fails:
1. Check the server logs in `target/server.log`
2. Review the HTML files saved in `target/` for debugging
3. Ensure port 9000 is available
4. Verify internet access to `casserverpac4j.herokuapp.com`

### Exit codes

- `0`: All tests passed successfully
- `1`: One or more tests failed
- `127`: Command not found (sbt installation issue)