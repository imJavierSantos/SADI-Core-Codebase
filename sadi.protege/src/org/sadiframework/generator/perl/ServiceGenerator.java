/**
 * 
 */
package org.sadiframework.generator.perl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Eddie Kawas
 * 
 */
public class ServiceGenerator {

    private boolean windowsPC = false;

    private String perlPath, lib, scriptsLocation;
    private ArrayList<String> libs = new ArrayList<String>();

    public static final String GEN_SERVICE_SCRIPT_NAME = "sadi-generate-services.pl";
    public static final String WIN_GEN_SERVICE_SCRIPT_NAME = "sadi-generate-services.bat";

    public static final String GEN_OWL_SCRIPT_NAME = "sadi-generate-datatypes.pl";
    public static final String WIN_GEN_OWL_SCRIPT_NAME = "sadi-generate-datatypes.bat";

    public ServiceGenerator() {
        this("perl", "", "");
    }

    public ServiceGenerator(String perl, String lib, String scripts) {
        if (perl != null)
            perlPath = perl;
        if (lib != null)
            this.lib = lib;
        if (scripts != null)
            scriptsLocation = scripts;
        if (System.getProperty("os.name").startsWith("Windows"))
            windowsPC = true;
    }

    private void validate() {
        // check that perlPath actually is an executable file
        if (!getPerlPath().trim().equals("")) {
            try {
                File file = new File(getPerlPath());
                if (!file.isFile() || !file.canExecute()) {
                    setPerlPath("");
                }
            } catch (Exception e) {
                setPerlPath("");
            }
        }
        // check that libs are correct ...
        if (!getLib().trim().equals("")) {
            this.libs = new ArrayList<String>();
            try {
                String[] libs = getLib().split(";");
                for (String l : libs) {
                    try {
                        File file = new File(l);
                        if (file.isDirectory()) {
                            this.libs.add(l);
                        }
                    } catch (Exception ex) {
                    }
                }
            } catch (Exception e) {
            }
        }
        // check that the script location exists if it is set
        if (!getScriptsLocation().trim().equals("")) {
            try {
                File file = new File(getScriptsLocation());
                if (!file.isDirectory()) {
                    setScriptsLocation("");
                }
            } catch (Exception e) {
                setScriptsLocation("");
            }
        }
    }

    public String generateService(String servicename, boolean isAsync) throws IOException, InterruptedException {
        validate();
        ArrayList<String> command = new ArrayList<String>();
        Process p;
        StringBuffer inputstream = new StringBuffer();
        StringBuffer errorstream = new StringBuffer();
        inputstream = new StringBuffer();
        errorstream = new StringBuffer();
        // construct the command to generate services ...
        // perl sadi-generate-services.pl [-A] service_name
        if (!getPerlPath().trim().equals("")) {
            if (!getScriptsLocation().trim().equals("")) {
                // add full path of script name
                command.add(getPerlPath());
                for (String l : this.libs)
                    command.add(String.format("-I%s", l));
                command.add(getScriptsLocation() + "/" + GEN_SERVICE_SCRIPT_NAME);
            } else {
                // add just the script name
                if (isWindowsPC()) {
                    command.add(WIN_GEN_SERVICE_SCRIPT_NAME);
                } else {
                    command.add(GEN_SERVICE_SCRIPT_NAME);
                }
            }
        } else {
            if (!getScriptsLocation().trim().equals("")) {
                // add full path of script name
                command.add(getScriptsLocation() + "/" + GEN_SERVICE_SCRIPT_NAME);
            } else {
                // add just the script name
                if (isWindowsPC()) {
                    command.add(WIN_GEN_SERVICE_SCRIPT_NAME);
                } else {
                    command.add(GEN_SERVICE_SCRIPT_NAME);
                }
            }
        }
        // isAsync
        if (isAsync)
            command.add("-A");
        // add the name of the service
        command.add(servicename);
        System.out.println(String.format("command: %s", command));

        // execute the command
        p = Runtime.getRuntime().exec(command.toArray(new String[] {}));
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        while ((line = br.readLine()) != null) {
            inputstream.append(line + System.getProperty("line.separator"));
        }
        BufferedReader errorBr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        line = null;
        while ((line = errorBr.readLine()) != null) {
            errorstream.append(line + System.getProperty("line.separator"));
        }
        p.waitFor();
        return String.format("OUTPUT:\n%s\nErrors:\n%s", inputstream.toString(), errorstream
                .toString());
    }

    public String generateDatatypes(String ontURL) throws IOException, InterruptedException {
        validate();
        ArrayList<String> command = new ArrayList<String>();
        Process p;
        StringBuffer inputstream = new StringBuffer();
        StringBuffer errorstream = new StringBuffer();
        inputstream = new StringBuffer();
        errorstream = new StringBuffer();
        // construct the command to generate services ...
        // perl sadi-generate-services.pl [-A] service_name
        if (!getPerlPath().trim().equals("")) {
            if (!getScriptsLocation().trim().equals("")) {
                // add full path of script name
                command.add(getPerlPath());
                for (String l : this.libs)
                    command.add(String.format("-I%s", l));
                command.add(getScriptsLocation() + "/" + GEN_OWL_SCRIPT_NAME);
            } else {
                // add just the script name
                if (isWindowsPC()) {
                    command.add(WIN_GEN_OWL_SCRIPT_NAME);
                } else {
                    command.add(GEN_OWL_SCRIPT_NAME);
                }
            }
        } else {
            if (!getScriptsLocation().trim().equals("")) {
                // add full path of script name
                command.add(getScriptsLocation() + "/" + GEN_OWL_SCRIPT_NAME);
            } else {
                // add just the script name
                if (isWindowsPC()) {
                    command.add(WIN_GEN_OWL_SCRIPT_NAME);
                } else {
                    command.add(GEN_OWL_SCRIPT_NAME);
                }
            }
        }
        // add switch to use url
        command.add("-u");
        // add the name of the service
        command.add(ontURL);
        System.out.println(String.format("command: %s", command));

        // execute the command
        p = Runtime.getRuntime().exec(command.toArray(new String[] {}));
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        while ((line = br.readLine()) != null) {
            inputstream.append(line + System.getProperty("line.separator"));
        }
        BufferedReader errorBr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        line = null;
        while ((line = errorBr.readLine()) != null) {
            errorstream.append(line + System.getProperty("line.separator"));
        }
        p.waitFor();
        return String.format("OUTPUT:\n%s\nErrors:\n%s", inputstream.toString(), errorstream
                .toString());
    }

    /**
     * @return the perl path
     */
    public String getPerlPath() {
        return this.perlPath;
    }

    /**
     * @param perlPath
     *            the path to perl to set
     */
    public void setPerlPath(String perlPath) {
        if (perlPath != null)
            this.perlPath = perlPath;
    }

    /**
     * @return a semi-colon delimited list of directories to add to INC
     */
    public String getLib() {
        return this.lib;
    }

    /**
     * @param lib
     *            a semi-colon delimited list of directories to add to INC
     */
    public void setLib(String lib) {
        libs = new ArrayList<String>();
        if (lib != null)
            this.lib = lib;
    }

    /**
     * @return the directory containing our SADI scripts
     */
    public String getScriptsLocation() {
        return this.scriptsLocation;
    }

    /**
     * @param loc
     *            the directory containing our SADI scripts
     */
    public void setScriptsLocation(String loc) {
        if (loc != null)
            this.scriptsLocation = loc;
    }

    public boolean isWindowsPC() {
        return this.windowsPC;
    }

}
