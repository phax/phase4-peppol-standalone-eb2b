/*
 * Copyright (C) 2015-2025 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phase4.peppolstandalone;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.helger.peppol.utils.PeppolCertificateChecker;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.phase4.dump.AS4DumpManager;
import com.helger.phase4.dump.AS4IncomingDumperFileBased;
import com.helger.phase4.dump.AS4OutgoingDumperFileBased;
import com.helger.phase4.peppol.Phase4PeppolSender;
import com.helger.phase4.sender.EAS4UserMessageSendResult;
import com.helger.security.certificate.CertificateHelper;
import com.helger.servlet.mock.MockServletContext;
import com.helger.web.scope.mgr.WebScopeManager;
import com.helger.xml.serialize.read.DOMReader;

/**
 * Special main class with a constant receiver. This one skips the SMP lookup.
 *
 * @author Philip Helger
 */
public final class MainPhase4PeppolSenderLocalHost8080EB2B
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainPhase4PeppolSenderLocalHost8080EB2B.class);

  public static void main (final String [] args)
  {
    WebScopeManager.onGlobalBegin (MockServletContext.create ());

    // Dump (for debugging purpose only)
    AS4DumpManager.setIncomingDumper (new AS4IncomingDumperFileBased ());
    AS4DumpManager.setOutgoingDumper (new AS4OutgoingDumperFileBased ());

    try
    {
      final Element aPayloadElement = DOMReader.readXMLDOM (new File ("src/test/resources/external/" +
                                                                      "example-invoice.xml")).getDocumentElement ();
      if (aPayloadElement == null)
        throw new IllegalStateException ("Failed to read XML file to be send");

      // Start configuring here
      final IParticipantIdentifier aReceiverID = Phase4PeppolSender.IF.createParticipantIdentifierWithDefaultScheme ("9915:helger");
      final EAS4UserMessageSendResult eResult;
      eResult = Phase4PeppolSender.builder ()
                                  .documentTypeID (Phase4PeppolSender.IF.createDocumentTypeIdentifierWithDefaultScheme ("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1"))
                                  .processID (Phase4PeppolSender.IF.createProcessIdentifierWithDefaultScheme ("urn:fdc:peppol.eu:2017:poacc:billing:01:1.0"))
                                  .senderParticipantID (Phase4PeppolSender.IF.createParticipantIdentifierWithDefaultScheme ("9915:phase4-test-sender"))
                                  .receiverParticipantID (aReceiverID)
                                  .senderPartyID ("000001")
                                  .countryC1 ("AT")
                                  .payload (aPayloadElement)
                                  .peppolAP_CAChecker (PeppolCertificateChecker.peppolTestEb2bAP ())
                                  .receiverEndpointDetails (CertificateHelper.convertStringToCertficate ("-----BEGIN CERTIFICATE-----\r\n" +
                                                                                                         "MIIFxjCCA66gAwIBAgIQD1VgGjChC3Qk/AMZpo/bVzANBgkqhkiG9w0BAQsFADBw\r\n" +
                                                                                                         "MQswCQYDVQQGEwJCRTEZMBcGA1UEChMQT3BlblBFUFBPTCBBSVNCTDEWMBQGA1UE\r\n" +
                                                                                                         "CxMNRk9SIFRFU1QgT05MWTEuMCwGA1UEAxMlUEVQUE9MIEVCMkIgQUNDRVNTIFBP\r\n" +
                                                                                                         "SU5UIFRFU1QgQ0EgLSBHMjAeFw0yNDEwMDEwMDAwMDBaFw0yNjA5MjEyMzU5NTla\r\n" +
                                                                                                         "ME0xCzAJBgNVBAYTAkJFMRMwEQYDVQQKDApPcGVuUGVwcG9sMRUwEwYDVQQLDAxF\r\n" +
                                                                                                         "QjJCIFRFU1QgQVAxEjAQBgNVBAMMCVBHRDAwMDAwMTCCASIwDQYJKoZIhvcNAQEB\r\n" +
                                                                                                         "BQADggEPADCCAQoCggEBAMzI2UcjGbyHW0GFe6qrNHlstCuFybSfqoyG+0F6UB+a\r\n" +
                                                                                                         "Lh0lGnln1rABHY+/Y2nVA+mjv3aUgZW5FzckbH0YCM7c9fegbbl9FCcONAnVY6uR\r\n" +
                                                                                                         "2D1NMl/Bty+3rYBT2ZoBFBrUiIPwYUnjjdkodlE8YDIhyGZ7dBj/bjUIyh9l+laI\r\n" +
                                                                                                         "bxQK3garaRcTY2xzy4KoIpN3nQ1Q7a7bUIWll4Bn37zoLlTc5xN6wpULI/orRHVH\r\n" +
                                                                                                         "NzFVU3zab9qqclcmH0PVeLAESioWGufzEott8qakHewDsAjbMZuISvnHU/03UYAx\r\n" +
                                                                                                         "HRvXqrLf83gmmsnJJjwyQov3MUX7c0Oa9mLdIYIhZt8CAwEAAaOCAX0wggF5MAwG\r\n" +
                                                                                                         "A1UdEwEB/wQCMAAwDgYDVR0PAQH/BAQDAgPoMBYGA1UdJQEB/wQMMAoGCCsGAQUF\r\n" +
                                                                                                         "BwMCMB0GA1UdDgQWBBRJ+cOyMF2jT6lhj2eZG7PqX33E3jBdBgNVHR8EVjBUMFKg\r\n" +
                                                                                                         "UKBOhkxodHRwOi8vcGtpLWNybC5zeW1hdXRoLmNvbS9jYV9jNjM3N2IwMDZiY2Ni\r\n" +
                                                                                                         "ZmIwZTcyY2IzMmYwYWNmN2I0Yy9MYXRlc3RDUkwuY3JsMDgGCCsGAQUFBwEBBCww\r\n" +
                                                                                                         "KjAoBggrBgEFBQcwAYYcaHR0cDovL3BraS1vY3NwLmRpZ2ljZXJ0LmNvbTAfBgNV\r\n" +
                                                                                                         "HSMEGDAWgBSU/+APrYa/669ggeUewv+vqwcKoTAtBgpghkgBhvhFARADBB8wHQYT\r\n" +
                                                                                                         "YIZIAYb4RQEQAQIDAQGI6+DccBYGOTU3NjA4MDkGCmCGSAGG+EUBEAUEKzApAgEA\r\n" +
                                                                                                         "FiRhSFIwY0hNNkx5OXdhMmt0Y21FdWMzbHRZWFYwYUM1amIyMD0wDQYJKoZIhvcN\r\n" +
                                                                                                         "AQELBQADggIBAGCHX7FtYNKgWCrLOgd72/gJ6IFcWbBTZTSpYJdkODma/sa+v6E7\r\n" +
                                                                                                         "VZ43CUSoOc4GfMSJuDar+c1gAjEZiTIE4i1+5UJmOBsd7W76T+71+0+x6IoxzQ1b\r\n" +
                                                                                                         "65BYShqxDOaXfaRuyAdebOFwAQPefk9kzI+On6D8mdh2WhOgG4cgecetPHIxghiK\r\n" +
                                                                                                         "mG14KcqL55/WxTCUg9ncGgQmSDCoq0bMP6e6Asjm1XNfXSYqr3v3JFFOcjgWsoL+\r\n" +
                                                                                                         "BdxcMpe6NlzSdwzpbJCbTAo2MdOdaZ183zYYca9aJZXd/pc22Tc68Yjj0ZZpNja1\r\n" +
                                                                                                         "LF8Wrv/Gj+y4Zh7N3UQaUmgckiMMzpYy2Xx7RDE8E90mwvaqiLT2KTxc0uMIMQHU\r\n" +
                                                                                                         "4aFzK2DnPZ0Uq9dWLKHmXchpgUV8xr8py0cH/C454F+wU3wX4rAUjTS8fAIaf/FA\r\n" +
                                                                                                         "5MZWKlRcAs6ljDk1TIUx1kLyDauERWq1HXiLUUdRPUr1MuDW+Ob6xBj4TBtBmISh\r\n" +
                                                                                                         "RHU/g3xT/cHsENZlkaDgUrIhncoIGt1xg3bXg2brsHOrl4sSv50ulegR+q87XQJ1\r\n" +
                                                                                                         "oktbmxvgkCdt80z2/SaqI/zEOY11YPbmOZTtuwa+oDse1RvbQAbHA65dwZY/pUr/\r\n" +
                                                                                                         "fHMfPqgVFEINuVfIV8C7HPJS2t2bxWMP9JeXAaMadvyF6s6fb/JaXJFw\r\n" +
                                                                                                         "-----END CERTIFICATE-----"),
                                                            "http://localhost:8080/as4")
                                  .sendMessageAndCheckForReceipt ();
      LOGGER.info ("Peppol send result: " + eResult);
    }
    catch (final Exception ex)
    {
      LOGGER.error ("Error sending Peppol message via AS4", ex);
    }
    finally
    {
      WebScopeManager.onGlobalEnd ();
    }
  }
}
