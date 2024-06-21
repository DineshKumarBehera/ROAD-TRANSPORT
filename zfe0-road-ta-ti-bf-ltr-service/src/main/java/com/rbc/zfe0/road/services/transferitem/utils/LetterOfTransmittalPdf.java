package com.rbc.zfe0.road.services.transferitem.utils;

import java.awt.Color;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rbc.zfe0.road.services.transferitem.entity.Issue;
import com.rbc.zfe0.road.services.transferitem.entity.TransferAgent;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;



/**
 * @author Tim Tracy
 */

@Slf4j
public class LetterOfTransmittalPdf
{
    PdfPTable newIssueHdrTable =null;
    DecimalFormat valueFormatter = new DecimalFormat("###,###,###,###.00");
    SimpleDateFormat dateFormatterMMDDYYYY = new SimpleDateFormat( "MM/dd/yyyy");
    private static final int VALUE_LENGTH = 18;

    /**
     */
    public void generate(TransferItem editTransferItem, OutputStream outputStream, boolean print ) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss");

        String todaysDate = dateFormatter.format( new Date() );

        // step 1: creation of a document-object
        Document document = new Document( PageSize.LETTER, 18, 18, 18, 18 );
        OutputStream fileOutputStream = null;

        try {
            // step 2:
            // we create a writer
            PdfWriter pdfWriter = PdfWriter.getInstance( document, outputStream );

            //pdfWriter.setViewerPreferences( PdfWriter.HideWindowUI );

            // step 3: we open the document
            document.open();

            if( print == true ) {
                pdfWriter.addJavaScript("this.print(false);", false);
            }

            PdfPCell cell = null;

            PdfPTable tempTable = this.createHeaderTable( document, editTransferItem );

            document.add( tempTable );

            tempTable = this.createTopSectionTable( document, editTransferItem );

            PdfPTable tempTableMain = new PdfPTable( 2 );

            tempTableMain.setWidthPercentage( 100 );
            tempTableMain.setTotalWidth( document.right() - document.left() );

            cell = new PdfPCell( tempTable );
            cell.setBorderWidth( .5f );
            cell.setColspan( 2 );

            tempTableMain.addCell( cell );


            document.add( tempTableMain );

            tempTable = this.createMidSectionTable( document, editTransferItem );

            document.add( tempTable );

            // Bottom section

            tempTable = this.createBottomSectionTable( document, editTransferItem );

            tempTableMain = new PdfPTable( 2 );

            tempTableMain.setWidthPercentage( 100 );
            tempTableMain.setTotalWidth( document.right() - document.left() );

            cell = new PdfPCell( tempTable );
            cell.setBorderWidth( .5f );
            cell.setColspan( 2 );

            tempTableMain.addCell( cell );

            document.add( tempTableMain );
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        // step 5: we close the document
        document.close();
    }
    private Properties getLetterProperties() {
        String propertiesFilename = "letterOfTransmittal.properties";
        Properties propertiesRetVal = new Properties();

        // look for a letterOfTransmittal.properties file
        InputStream inputStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream( "letterOfTransmittal.properties");

        if( inputStream != null ) {
            try {
                propertiesRetVal.load( inputStream );
            } catch (IOException e) {
                log.error( "Error loading " + propertiesFilename, e);
                e.printStackTrace();
            }
        }
        return propertiesRetVal;
    }

    private String getTextFromProperties( String property ) {
        String retVal = null;

        Properties letterProperties = getLetterProperties();

        String propertyText = letterProperties.getProperty( property );

        if( propertyText != null ) {
            // text could contain literal \n where there should be
            // new lines
            retVal = propertyText.replaceAll( "\\n", "\n" );
        }

        return retVal;
    }

    private String getCertificationText() {
        String retVal = "";

        String certification = getTextFromProperties( "certification");

        if( certification != null ) {
            retVal = certification;
        } else {
            retVal =
                    "Under penalties of perjury, I certify that:" +"\n" +
                            "1. The number shown on this form is my correct taxpayer identification number" +
                            " (or I am waiting for a number to be issued to me), and " +"\n" +
                            "2. I am not subject to backup withholding because: " +
                            "(a) I am exempt from backup withholding, or " +
                            "(b) I have not been notified by the Internal Revenue Service (IRS)" +
                            " that I am subject to backup withholding as a result of a failure" +
                            " to report all interest or dividends, or (c)" +
                            " the IRS has notified me that I am no longer subject to backup " +
                            "withholding, and " +"\n" +
                            "3. I am a U.S. citizen or other U.S. person, and" +"\n" +
                            "4. No FATCA exemption code is required because the account is maintained in the United States "+
                            "by a U.S. financial institution."+"\n" +
                            "You must cross out item 2 above if you have been notified by the IRS that you are currently subject "+
                            "to backup withholding because you have failed to report all interest and dividends on your tax return."+
                            "By entering your name below, you signify that you have read, met, and agreed to all terms and conditions above."+
                            "The IRS does not require your consent to any provision of this document other than the certification "+
                            "required to avoid backup withholding."+
                            "You may cross out this entire Substitute W-9 Certification section if you are not a U.S. citizen or other U.S. person."+
                            " In such case, an appropriate Form W-8 must be submitted."+
                            "\n" +
                            "As agent, we hereby agree for ourselves, our successors, assigns, heirs, executors " +
                            "or administrators, at all times now and hereafter to indemnify and save " +
                            "harmless you as transfer agent and your principal and your successors " +
                            "and assigns from and against all loss or damage that may arise by reason" +
                            " thereof, and against all costs, charges and expense, all actions or" +
                            " suits whether groundless or otherwise, it being the purpose of the " +
                            "agreement of indemnity to duly protect both you and your principal," +
                            " your successors and assigns in the premises, RBC Capital Markets, LLC.";

        }
        return retVal;
    }
    private String getSignatureName() {
        String retVal = "";

        String signatureName = getTextFromProperties( "signatureName");

        if( signatureName != null ) {
            retVal = signatureName;
        } else {
            retVal = "Rozanne Fread";
        }
        return retVal;
    }


    private String getContactInformation() {
        String retVal = "";

        String contactInfo = getTextFromProperties( "contactInfo");

        if( contactInfo != null ) {
            retVal = contactInfo;
        } else {
            StringBuffer buff = new StringBuffer();

            buff.append( "RBC Capital Markets, LLC\n")
                    .append("Attn: Physical Security Processing, P9\n")
                    .append("60 S. 6th Street\n")
                    .append("Minneapolis, MN 55402\n")
                    .append( "Phone: 612-607-8414");


            retVal = buff.toString();
        }
        return retVal;
    }

    private PdfPTable createBottomSectionTable(Document documentIn, TransferItem transferItem) throws DocumentException {
        int deliveryInstructionsLabelFontSize = 12;
        int deliveryInstructionsLabelFontWeight = Font.BOLD;

        int deliveryInstructionsFontSize = 8;
        int deliveryInstructionsFontWeight = Font.BOLD;

        int contactInformationLabelFontSize = 12;
        int contactInformationLabelFontWeight = Font.BOLD;

        int contactInformationFontSize = 8;
        int contactInformationFontWeight = Font.BOLD;

        int companyNameFontSize = 8;
        int companyNameFontWeight = Font.NORMAL;

        PdfPTable bottomSectionTable = new PdfPTable( 2 );

        int colWidths[] = { 50, 50 };
        bottomSectionTable.setWidths( colWidths );
        bottomSectionTable.setWidthPercentage( 100 );

        bottomSectionTable.setTotalWidth( documentIn.right() - documentIn.left() );

        bottomSectionTable.getDefaultCell().setPadding(5);

        PdfPTable bottomSectionLeft = new PdfPTable( 1 );
        //DELIVERY INSTRUCTIONS
        PdfPCell cell = new PdfPCell(new Phrase( "DELIVERY INSTRUCTIONS",
                getFont( deliveryInstructionsLabelFontSize, deliveryInstructionsLabelFontWeight ) ) );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setBorderWidthBottom( (float) .5 );
        cell.setBorderWidthRight( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);
        bottomSectionLeft.addCell(cell);

        Phrase phrase = new Phrase(
                transferItem.getDeliveryInstruction().getAddressBox(),
                getFont( deliveryInstructionsFontSize, deliveryInstructionsFontWeight ));
        cell = new PdfPCell( phrase );
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
        cell.setPadding( 4f );
        cell.setUseDescender(true);
        bottomSectionLeft.addCell(cell);

        /*cell = new PdfPCell( new Phrase( "CONTACT INFORMATION",
                getFont( contactInformationLabelFontSize, contactInformationLabelFontWeight ) ) );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( .5f );
        cell.setBorderWidthTop( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);*/

        //bottomSectionLeft.addCell(cell);

        /*cell = new PdfPCell( new Phrase( getContactInformation(),
                getFont( contactInformationFontSize, contactInformationFontWeight ) ) );
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( .5f );
        cell.setBorderWidthRight( 0 );
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setColspan(1);
        cell.setPadding( 4f );
        cell.setUseDescender(true);*/

        //bottomSectionLeft.addCell(cell);

        cell = new PdfPCell( bottomSectionLeft );

        cell.setUseVariableBorders(true);
        cell.setBorderWidth( 0 );
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthRight( .5f );
        cell.setBorderWidthBottom( 0 );

        // Add the Right section to the table
        bottomSectionTable.addCell( cell );

        PdfPTable bottomSectionRight = new PdfPTable( 1 );


        PdfPTable pdfTable = getTaxPayerIdTable( transferItem );

        cell = new PdfPCell( pdfTable );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthRight( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthTop( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);

        bottomSectionRight.addCell(cell);

        //pdfTable = getCertificationTable();
        //pdfTable = getContactInformation();
        cell = new PdfPCell( new Phrase( getContactInformation(),
                getFont( contactInformationFontSize, contactInformationFontWeight ) ) );
        cell = new PdfPCell( pdfTable );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthRight( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthTop( 0 );
        cell.setColspan(1);
        cell.setPadding( 4f );
        cell.setUseDescender(true);

        //    bottomSectionRight.addCell(cell);

        phrase = new Phrase( getContactInformation(),
                getFont( companyNameFontSize, companyNameFontWeight ));

        cell = new PdfPCell( phrase );
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment( Element.ALIGN_TOP );
//        cell.setUseVariableBorders(true);
//        cell.setBorderWidthRight( 0 );
//        cell.setBorderWidthLeft( 0 );
//        cell.setBorderWidthTop( 0 );
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);

        bottomSectionRight.addCell(cell);

        // Create a cell from the right table
        cell = new PdfPCell( bottomSectionRight );

        cell.setUseVariableBorders(true);
        cell.setBorderWidth( 0 );
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthRight( .5f );
        cell.setBorderWidthBottom( 0 );

        // Add the right section to the table
        bottomSectionTable.addCell(cell);

        return bottomSectionTable;
    }
    private PdfPTable createMidSectionTable(Document documentIn, TransferItem transferItem) throws DocumentException {
        int certificatesPresentedLabelFontSize = 6;
        int certificatesPresentedLabelFontWeight = Font.BOLD;

        int certificatesPresentedFontSize = 10;
        int certificatesPresentedFontWeight = Font.NORMAL;

        int registrationLabelFontWeight = Font.BOLD;

        PdfPTable midSectionTable = new PdfPTable( 3 );

        int colWidths[] = { 70, 16, 14 };
        midSectionTable.setWidths( colWidths );
        midSectionTable.setWidthPercentage( 100f );

        midSectionTable.setTotalWidth( documentIn.right() - documentIn.left() );

        midSectionTable.getDefaultCell().setPadding(5);

        PdfPTable securityDescTable = getMidRowCellTable( "DESCRIPTION OF SECURITY PRESENTED:",
                getSecurityInfoForLetter( transferItem ), Element.ALIGN_LEFT, Element.ALIGN_LEFT, false );

        PdfPCell cell = new PdfPCell( securityDescTable );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setColspan(1);
        cell.setPaddingLeft( 4f );
        cell.setUseDescender(true);

        midSectionTable.addCell(cell);

        PdfPTable cusipTable = getMidRowCellTable( "CUSIP:",
                transferItem.getOriginalCusIp(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, false );

        cell = new PdfPCell( cusipTable );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setColspan(1);
        cell.setPaddingLeft( 4f );
        cell.setUseDescender(true);

        midSectionTable.addCell(cell);

        PdfPTable securityNbrTable = getMidRowCellTable( "SECURITY #:",
                transferItem.getOriginalAdpSecurityNumber(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, true );

        cell = new PdfPCell( securityNbrTable );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setColspan(1);
        cell.setPaddingLeft( 4f );
        cell.setUseDescender(true);

        midSectionTable.addCell(cell);

        cell = new PdfPCell( new Phrase("TRANSACTION INFORMATION:",
                getFont( certificatesPresentedLabelFontSize, certificatesPresentedLabelFontWeight )) );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setColspan( 3 );
        cell.setPaddingLeft( 4f );
        cell.setUseDescender(true);
        midSectionTable.addCell(cell);

        List<Issue> issues = transferItem.getIssue();
        if (issues != null && !issues.isEmpty()) {
            Issue firstIssue = issues.get(0);
            if (firstIssue != null) {
                String originalDenominatorDescr = firstIssue.getDenomination();
                if (originalDenominatorDescr != null) {
                    cell = new PdfPCell(
                            new Phrase(
                                    originalDenominatorDescr.trim(),
                                    getFont(certificatesPresentedFontSize, certificatesPresentedFontWeight)
                            )
                    );
                } else {
                    cell = new PdfPCell(
                            new Phrase(
                                    "N/A",
                                    getFont(certificatesPresentedFontSize, certificatesPresentedFontWeight)
                            )
                    );
                }
            } else {
                cell = new PdfPCell(
                        new Phrase(
                                "No issues",
                                getFont(certificatesPresentedFontSize, certificatesPresentedFontWeight)
                        )
                );
            }
        } else {
            cell = new PdfPCell(
                    new Phrase(
                            "No issues",
                            getFont(certificatesPresentedFontSize, certificatesPresentedFontWeight)
                    )
            );
        }



        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setColspan( 3 );
        cell.setPadding( 4f );

        cell.setUseDescender(true);
        cell.setMinimumHeight( 72f );
        midSectionTable.addCell(cell);

        PdfPTable newIssueHdrTable = getNewIssueTableHdr();
        cell = new PdfPCell( newIssueHdrTable );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setBorderWidthLeft( 1 );
        cell.setBorderWidthRight( 1 );
        cell.setColspan( 3 );
        cell.setUseDescender(true);
        midSectionTable.addCell(cell);

        PdfPTable newIssueTable = getNewIssueTable( transferItem );
        cell = new PdfPCell( newIssueTable );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setBorderWidthLeft( 1 );
        cell.setBorderWidthRight( 1 );
        cell.setColspan( 3 );
        cell.setPaddingTop( 6f );
        cell.setPaddingLeft( 4f );
        cell.setPaddingRight( 4f );
        cell.setPaddingBottom( 6f );
        cell.setUseDescender(true);
        midSectionTable.addCell(cell);

        return midSectionTable;
    }

    private String getSecurityInfoForLetter(TransferItem transferItem) {
        StringBuffer buff = new StringBuffer();

        if( transferItem.getOriginalQty() != null ) {
            buff.append( " Quantity: " + transferItem.getOriginalQty() );
        }
        if( transferItem.getOriginalSecurityDescr()!= null ) {
            buff.append( " Desc: ");
            buff.append( transferItem.getOriginalSecurityDescr() );
        }

        return buff.toString();
    }
    private PdfPTable getCertificationTable() throws DocumentException, IOException {
        int certificationFontSize = 6;
        int certificationFontWeight = Font.NORMAL;

        int signatureFontSize = 8;
        int signatureFontWeight = Font.NORMAL;

        int signatureNameFontSize = 8;
        int signatureNameFontWeight = Font.NORMAL;

        PdfPTable pdfTable = new PdfPTable(3);
        int[] columnWidths = { 15, 67, 18 };
        pdfTable.setWidths( columnWidths );
        pdfTable.setWidthPercentage( 100f );
        PdfPCell cell = null;
        Phrase phrase = null;

        // Add the certification phrase
        phrase = new Phrase( getCertificationText(),
                getFont( certificationFontSize, certificationFontWeight ));

        cell = new PdfPCell( phrase );
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth( 0 );
        cell.setColspan(3);
        cell.setUseDescender(true);
        pdfTable.addCell( cell );


        // The next row will consist of 3 cells

        // 1. Add the "signature" text
        phrase = new Phrase( "Signature: ",
                getFont( signatureFontSize, signatureFontWeight ));

        cell = new PdfPCell( phrase );
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment( Element.ALIGN_CENTER );
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);
        pdfTable.addCell( cell );

        // 2. Add the signature graphic
        Image signatureImg = getImage( "letterOfTransmittalSignature.bmp" );

        cell = new PdfPCell( signatureImg, true );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth( 0 );

        cell.setColspan(1);
        pdfTable.addCell( cell );

        // 3. Add the date
        String dateStr = this.dateFormatterMMDDYYYY.format( new Date() );

        phrase = new Phrase( dateStr,
                getFont( signatureFontSize, signatureFontWeight ));

        cell = new PdfPCell( phrase );
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment( Element.ALIGN_CENTER );
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);
        pdfTable.addCell( cell );

        // The next row will only be one cell
        // add the signature name
        phrase = new Phrase( getSignatureName(),
                getFont( signatureNameFontSize, signatureNameFontWeight ));

        cell = new PdfPCell( phrase );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth( 0 );
        cell.setColspan(3);
        cell.setUseDescender(true);
        pdfTable.addCell( cell );

        return pdfTable;
    }

    private PdfPTable getTaxPayerIdTable( TransferItem transferItem ) {
        int formW9LabelFontSize = 8;
        int formW9LabelFontWeight = Font.NORMAL;
        int formW9FontSize = 10;
        int formW9FontWeight = Font.BOLD;

        int taxIdLabelFontSize = 12;
        int taxIdLabelFontWeight = Font.BOLD;
        int taxIdFontSize = 9;
        int taxIdFontWeight = Font.NORMAL;

        PdfPTable pdfTable = new PdfPTable(1);
        PdfPCell cell = null;
        Phrase phrase = null;
        Chunk chunk = null;

        // Add the FORM W-9 line
        phrase = new Phrase();

        /*chunk = new Chunk( "CONTACT INFORMATION",
                getFont( formW9LabelFontSize, formW9LabelFontWeight    ));
        phrase.add( chunk );

        chunk = new Chunk( "W-9",
                getFont( formW9FontSize, formW9FontWeight ));
        phrase.add( chunk );

        chunk = new Chunk( " Department of the Treasury Internal Revenue Service",
                getFont( formW9LabelFontSize, formW9LabelFontWeight    ));
        phrase.add( chunk );*/

        /*cell = new PdfPCell( phrase );
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);
        pdfTable.addCell( cell );*/

        // Add the TAXPAYER IDENTIFICATION NUMBER line
        phrase = new Phrase();

        chunk = new Chunk( "CONTACT INFORMATION",
                getFont( taxIdLabelFontSize, taxIdLabelFontWeight));
        phrase.add( chunk );

    /*    chunk = new Chunk( getContactInformation(),
                getFont( taxIdFontSize, taxIdFontWeight ));
        phrase.add( chunk );*/

        cell = new PdfPCell( phrase );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);
        pdfTable.addCell( cell );

        return pdfTable;
    }
    private PdfPTable getNewIssueTable( TransferItem editTransferItem ) throws DocumentException {
        PdfPTable tableRetVal = new PdfPTable( 4 );
        int[] columnWidths = { 48, 16, 17, 19 };
        tableRetVal.setWidths( columnWidths );
        tableRetVal.setWidthPercentage( 100 );

        int issueCount = 0;

        Issue issue = new Issue();
        if (editTransferItem.getIssue() != null) {
            for (Issue issueCheck : editTransferItem.getIssue()) {
                populateNewIssueRow(tableRetVal, issueCheck);
                issueCount = 1;
            }
        }

        // Fill in the rest of the table with blank cells.
        Issue emptyIssue = new Issue();
        for (int i = issueCount; i < 5; i++) {
            populateNewIssueRow(tableRetVal, emptyIssue);
        }


        return tableRetVal;
    }

    private void populateNewIssueRow( PdfPTable tableIn, Issue issue ) {
        int newIssueFontSize = 10;
        int newIssueFontWeight = Font.NORMAL;
        String newIssueFontName = FontFactory.HELVETICA;
        BigDecimal quantity = null;
        String quantityStr = null;
        Phrase phrase =null;

        // Security description
        String securityDescription = issue.getSecurityDescription();
        phrase = new Phrase(securityDescription != null ? securityDescription.trim() : "",
                getFont(newIssueFontSize, newIssueFontWeight));

//        getFont( newIssueFontName, newIssueFontSize, newIssueFontWeight ) );

        PdfPCell cell = new PdfPCell( phrase );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth(.5f);
        cell.setBorderColor(new BaseColor(Color.LIGHT_GRAY.getRed(),
                Color.LIGHT_GRAY.getGreen(),
                Color.LIGHT_GRAY.getBlue()));
        cell.setColspan(1);
        cell.setUseDescender(true);


        tableIn.addCell(cell);

        // Cusip
        String cusip = issue.getCusip();
        phrase = new Phrase(cusip != null ? cusip.trim() : "",
                getFont(newIssueFontName, newIssueFontSize, newIssueFontWeight));


        cell = new PdfPCell( phrase );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth(.5f);
        cell.setBorderColor(new BaseColor(Color.LIGHT_GRAY.getRed(),
                Color.LIGHT_GRAY.getGreen(),
                Color.LIGHT_GRAY.getBlue()));
        cell.setColspan(1);
        cell.setUseDescender(true);


        tableIn.addCell(cell);

        // Quantity
        quantity = issue.getQuantity();
        quantityStr = null;

        if( quantity != null ) {
            quantityStr = quantity.toString();
        }

        phrase = new Phrase(quantityStr != null ? quantityStr.trim() : "",
                getFont(newIssueFontName, newIssueFontSize, newIssueFontWeight));

        cell = new PdfPCell( phrase );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth(.5f);
        cell.setBorderColor(new BaseColor(Color.LIGHT_GRAY.getRed(),
                Color.LIGHT_GRAY.getGreen(),
                Color.LIGHT_GRAY.getBlue()));
        cell.setColspan(1);
        cell.setUseDescender(true);

        tableIn.addCell(cell);

        // Denominations
        String denominations = null;

        if( issue.getCashEntryFlag()!=null ) {
            BigDecimal insValue = issue.getInsuranceValue();

            if( insValue != null ) {
                denominations = issue.getDenomination();
            }
        } else {
            denominations = issue.getDenomination();
        }

        if (denominations == null || denominations.trim().isEmpty()) {
            denominations = " ";
        }


        phrase = new Phrase( denominations ,
                getFont( newIssueFontName, newIssueFontSize, newIssueFontWeight ) );

        cell = new PdfPCell( phrase );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth(.5f);
        cell.setBorderColor(new BaseColor(Color.LIGHT_GRAY.getRed(),
                Color.LIGHT_GRAY.getGreen(),
                Color.LIGHT_GRAY.getBlue()));
        cell.setColspan(1);
        cell.setUseDescender(true);

        tableIn.addCell(cell);
    }


    private PdfPTable getNewIssueTableHdr() throws DocumentException {
        PdfPTable tableRetVal = new PdfPTable(4);
        int[] columnWidths = {48, 16, 17, 19};
        tableRetVal.setWidths(columnWidths);
        tableRetVal.setWidthPercentage(100);
        int newIssueHdrFontSize = 9;
        int newIssueHdrFontWeight = Font.BOLD;
        String newIssueHdrFontName = FontFactory.HELVETICA;

        PdfPCell cell;

        // Description of New Issue
        cell = new PdfPCell(new Phrase("Description of New Issue", getFont(newIssueHdrFontName, newIssueHdrFontSize, newIssueHdrFontWeight, Color.WHITE)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth(0);
        cell.setColspan(1);
        cell.setUseDescender(true);
        cell.setBackgroundColor(BaseColor.BLACK);
        tableRetVal.addCell(cell);



        // CUSIP
        cell = new PdfPCell(new Phrase("CUSIP", getFont(newIssueHdrFontName, newIssueHdrFontSize, newIssueHdrFontWeight, Color.WHITE)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth(0);
        cell.setColspan(1);
        cell.setUseDescender(true);
        cell.setBackgroundColor(BaseColor.BLACK);
        tableRetVal.addCell(cell);

        // New Quantity
        cell = new PdfPCell(new Phrase("New Quantity", getFont(newIssueHdrFontName, newIssueHdrFontSize, newIssueHdrFontWeight, Color.WHITE)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth(0);
        cell.setColspan(1);
        cell.setUseDescender(true);
        cell.setBackgroundColor(BaseColor.BLACK);
        tableRetVal.addCell(cell);

        // Denominations
        cell = new PdfPCell(new Phrase("Denominations", getFont(newIssueHdrFontName, newIssueHdrFontSize, newIssueHdrFontWeight, Color.WHITE)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth(0);
        cell.setColspan(1);
        cell.setUseDescender(true);
        cell.setBackgroundColor(BaseColor.BLACK);
        tableRetVal.addCell(cell);

        return tableRetVal;
    }
    /**
     * @param-document
     * @param transferItem
     * @return
     * @throws DocumentException
     */
    private PdfPTable createTopSectionTable(Document documentIn, TransferItem transferItem) throws DocumentException {
        int transferAgentFontSize = 12;
        int transferAgentNameFontSize = 9;
        String transferAgentNameFontFamily = FontFactory.TIMES_ROMAN;
        int transferAgentNameFontWeight = Font.NORMAL;

        int transferTypeLabelFontSize = 8;
        int transferTypeLabelFontWeight = Font.BOLD;
        int transferTypeFontSize = 8;
        int transferAgentPhoneLabelFontSize = 9;
        int transferAgentPhoneLabelFontWeight = Font.NORMAL;
        int transferAgentPhoneFontSize = 10;
        int transferAgentPhoneFontWeight = Font.NORMAL;

        int registrationLabelFontSize = 9;
        int registrationLabelFontWeight = Font.BOLD;

        int repIdLabelFontSize = 8;
        int itemIdLabelFontSize = 6;
        int itemIdFontSize = 8;


        TransferAgent transferAgent = transferItem.getTransferAgent();

        if( transferAgent == null ) {
            // Create an empty one
            transferAgent = new TransferAgent();
        }

        PdfPTable firstSectionTable = new PdfPTable( 2 );

        int colWidths[] = { 50, 50 };
        firstSectionTable.setWidths( colWidths );
        firstSectionTable.setWidthPercentage( 100 );

        firstSectionTable.setTotalWidth( documentIn.right() - documentIn.left() );

        firstSectionTable.getDefaultCell().setPadding(5);

        PdfPTable firstSectionLeft = new PdfPTable( 1 );

        PdfPCell cell = new PdfPCell(new Phrase( "TRANSFER AGENT",
                getFont( transferAgentFontSize, Font.BOLD ) ) );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setBorderWidthBottom( (float) .5 );
        cell.setBorderWidthRight( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);
        firstSectionLeft.addCell(cell);

        Phrase phrase = new Phrase();
        String transferAgentName = transferAgent.getTransferAgentName();
        if (transferAgentName != null) {
            phrase.add(new Chunk(transferAgentName.trim(),
                    getFont(transferAgentNameFontFamily, transferAgentNameFontSize, transferAgentNameFontWeight)));
        }

        String addressBox = transferAgent.getAddressBox();
        if (addressBox != null) {
            phrase.add(Chunk.NEWLINE);
            phrase.add(new Chunk(addressBox.trim(),
                    getFont(transferAgentNameFontFamily, transferAgentNameFontSize, transferAgentNameFontWeight)));
        }


        phrase.add( Chunk.NEWLINE );
        phrase.add( Chunk.NEWLINE );
        phrase.add( new Chunk( "Phone: ",
                getFont( transferAgentPhoneLabelFontSize, transferAgentPhoneLabelFontWeight ) ) );

        String phoneNumber = ROADFormatUtils.formatPhoneNumber( transferAgent.getPhoneNumber() );
        phrase.add( new Chunk( phoneNumber,
                getFont( transferAgentPhoneFontSize, transferAgentPhoneFontWeight ) ) );
        phrase.add( new Chunk( "   Fax: ",
                getFont( transferAgentPhoneLabelFontSize, transferAgentPhoneLabelFontWeight ) ) );

        String faxNumber = ROADFormatUtils.formatPhoneNumber( transferAgent.getFaxNumber() );

        phrase.add( new Chunk( faxNumber,
                getFont( transferAgentPhoneFontSize, transferAgentPhoneFontWeight ) ) );
        phrase.add( Chunk.NEWLINE );
        phrase.add( Chunk.NEWLINE );

        cell = new PdfPCell( phrase );
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
        cell.setPadding( 4f );
        cell.setUseDescender(true);
        cell.setMinimumHeight( 138f );
        firstSectionLeft.addCell(cell);


        String feeAmount = null;
        transferAgent = transferItem.getTransferAgent();
        if (transferAgent != null) {
            feeAmount = transferAgent.getFeeAmount();
        }

        Phrase transferFeePhrase;
        if (feeAmount != null) {
            transferFeePhrase = getTransferFeePhrase("TRANSFER FEE: ", feeAmount.trim());
        } else {
            transferFeePhrase = getTransferFeePhrase("TRANSFER FEE: ", "");
        }


        cell = new PdfPCell(transferFeePhrase);

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( .5f );
        cell.setBorderWidthRight( 0 );
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setColspan(1);
        cell.setPadding( 4f );
        cell.setUseDescender(true);

        firstSectionLeft.addCell(cell);

        cell = new PdfPCell(new Phrase( "THE CERTIFICATES LISTED BELOW ARE BEING SUBMITTED FOR:",
                getFont( transferTypeLabelFontSize, transferTypeLabelFontWeight ) ) );
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setColspan(1);
        cell.setPadding( 4f );
        cell.setUseDescender(true);
        firstSectionLeft.addCell( cell );

        cell = new PdfPCell(new Phrase( transferItem.getTleTransferType(),
                getFont( transferTypeLabelFontSize ) ) );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth( 0 );

        cell.setColspan(1);
        cell.setPadding( 4f );
        cell.setUseDescender(true);
        firstSectionLeft.addCell( cell );

        cell = new PdfPCell( firstSectionLeft );

        cell.setUseVariableBorders(true);
        cell.setBorderWidth( 0 );
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthRight( .5f );
        cell.setBorderWidthBottom( 0 );

        // Add the left section to the table
        firstSectionTable.addCell( cell );


        PdfPTable firstSectionRight = new PdfPTable( 5 );
        int colWidths5[] = { 20, 20, 20, 20, 20 };
        firstSectionRight.setWidths( colWidths5 );

        // rep id
        PdfPTable pdfTable = getTopRowCellTable( "Rep ID:", transferItem.getRepId() );

        cell = new PdfPCell( pdfTable );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthTop( 0 );
        cell.setBorderColor( new BaseColor(Color.LIGHT_GRAY.getRed() ));
        cell.setColspan(1);
        cell.setUseDescender(true);

        firstSectionRight.addCell(cell);

        // item id#
        pdfTable = getTopRowCellTable("ItemId#:", ((Number) transferItem.getTransferItemId()).intValue() + "");

        cell = new PdfPCell( pdfTable );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthTop( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);

        firstSectionRight.addCell(cell);

        // tax id#
        pdfTable = getTopRowCellTable( "SSN/EIN:", transferItem.getRegistrationTaxId() );

        cell = new PdfPCell( pdfTable );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthTop( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);

        firstSectionRight.addCell(cell);

        // alt branch
        pdfTable = getTopRowCellTable( "Alt Branch:", transferItem.getAltBranchCode() );

        cell = new PdfPCell( pdfTable );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthTop( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);

        firstSectionRight.addCell(cell);

        String transferDate = "";

        // transfer date
        if( transferItem.getTransferredDt() != null ) {
            transferDate = dateFormatterMMDDYYYY.format( transferItem.getTransferredDt() );
        } else {
            transferDate = dateFormatterMMDDYYYY.format( new Date() );
        }
        pdfTable = getTopRowCellTable( "Transfer Date:", transferDate );

        cell = new PdfPCell( pdfTable );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setBorderWidthTop( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);

        firstSectionRight.addCell(cell);

        cell = new PdfPCell(new Phrase( " TO BE REGISTERED IN THE NAME OF:",
                getFont( registrationLabelFontSize, registrationLabelFontWeight ) ) );
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setBackgroundColor( new BaseColor(Color.LIGHT_GRAY.getRed() ));
        cell.setColspan(5);
        cell.setUseDescender(true);
        firstSectionRight.addCell( cell );

        cell = new PdfPCell();

        // Add a paragraph to help us space things
        Paragraph pSpacer = new Paragraph(" ");

        cell.addElement( pSpacer );

        // Add the actual data
        phrase = new Phrase( transferItem.getRegistrationDescr(), getFont( transferTypeLabelFontSize ) );

        Paragraph pCenter = new Paragraph( phrase );
        cell.addElement( pCenter );

        // Add another paragraph to help us space things
        cell.addElement( pSpacer );

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment( Element.ALIGN_CENTER );

        cell.setUseVariableBorders(true);
        cell.setPadding( 4 );
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setBorderWidthBottom( .5f );

        // TODO: comment two lines
        //cell.setBorderWidth( 1 );
        //cell.setBorderColor( Color.RED );
        cell.setMinimumHeight( 118f );

        cell.setColspan(5);

        cell.setUseDescender(true);
        cell.addElement( pSpacer );
        firstSectionRight.addCell(cell);

        //cell = new PdfPCell( getTransferFeePhrase( "CBRS CONTROL ID", StringUtils.trimToEmpty( transferAgent.getFee() )));
        cell = new PdfPCell(new Phrase("CBRS CONTROL ID", getFont(transferTypeLabelFontSize)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBackgroundColor( new BaseColor(Color.LIGHT_GRAY.getRed()) );
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(.5f);
        cell.setColspan(5);
        cell.setPadding(4f);
        cell.setUseDescender(true);
        firstSectionRight.addCell(cell);

        //cell = new PdfPCell( getTransferFeePhrase( "123456789012345", StringUtils.trimToEmpty( transferAgent.getFee() )));
        cell = new PdfPCell(new Phrase(transferItem.getControlId(), getFont(transferTypeLabelFontSize)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setUseVariableBorders(true);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setColspan(5);
        cell.setPadding(4f);
        cell.setMinimumHeight(20f);
        cell.setUseDescender(true);
        firstSectionRight.addCell(cell);
        // Add a cell to help us space things
//        cell = new PdfPCell(new Phrase( " " ));
//        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//        cell.setVerticalAlignment( Element.ALIGN_CENTER );
//
//        cell.setUseVariableBorders(true);
//        cell.setPadding( 4 );
//        cell.setBorderWidth( 1 );
//        cell.setBorderColor( Color.RED );
//        cell.setColspan(5);
//        firstSectionRight.addCell( cell );


        cell = new PdfPCell( firstSectionRight );

        cell.setUseVariableBorders(true);
        cell.setBorderWidthTop( 0 );
        cell.setBorderWidthBottom( 0 );
        cell.setBorderWidthLeft( 0 );
        cell.setBorderWidthRight( 0 );
        cell.setBorderColor( new BaseColor(Color.LIGHT_GRAY.getRed()) );

// Add the left section to the table
        firstSectionTable.addCell( cell );

        return firstSectionTable;
    }

    private Phrase getTransferFeePhrase( String labelIn, String valueIn ) throws DocumentException {
        int labelFontSize = 8;
        int valueFontSize = 9;
        int labelFontWeight = Font.NORMAL;
        int valueFontWeight = Font.BOLD;


        Phrase phrase = new Phrase();

        Chunk chunk = new Chunk( labelIn, getFont( labelFontSize, labelFontWeight ));
        phrase.add( chunk );

        chunk = new Chunk(valueIn.trim(), getFont(valueFontSize, valueFontWeight));
        phrase.add( chunk );

        return phrase;
    }

    private PdfPTable getMidRowCellTable( String labelIn, String valueIn, int alignmentTop,
                                          int alignmentBottom, boolean rightBorder ) {
        int labelFontSize = 6;
        int valueFontSize = 9;
        int labelFontWeight = Font.BOLD;
        int valueFontWeight = Font.BOLD;

        PdfPTable pdfTable = new PdfPTable(1);
        pdfTable.setSpacingAfter( (float) 1.25 );

        PdfPCell cell = new PdfPCell(new Phrase(labelIn.trim(), getFont(labelFontSize, labelFontWeight)));
        cell.setHorizontalAlignment( alignmentTop );
        if( rightBorder == false ) {
            cell.setBorderWidth( 0 );
        } else {
            cell.setUseVariableBorders( true );
            cell.setBorderWidthTop( 0 );
            cell.setBorderWidthBottom( 0 );
            cell.setBorderWidthLeft( 0 );
            cell.setBorderWidthRight( .5f );
        }
        cell.setColspan(1);
        cell.setUseDescender(true);


        pdfTable.addCell( cell );

        cell = new PdfPCell(new Phrase(valueIn.trim(), getFont(valueFontSize, valueFontWeight)));
        cell.setHorizontalAlignment( alignmentBottom );

        if( rightBorder == false ) {
            cell.setBorderWidth( 0 );
        } else {
            cell.setUseVariableBorders( true );
            cell.setBorderWidthTop( 0 );
            cell.setBorderWidthBottom( 0 );
            cell.setBorderWidthLeft( 0 );
            cell.setBorderWidthRight( .5f );
        }

        cell.setColspan(1);
        cell.setUseDescender(true);
        cell.setPaddingTop( 0 );
        pdfTable.addCell( cell );

        return pdfTable;
    }

    private PdfPTable getTopRowCellTable( String labelIn, String valueIn ) {
        int labelFontSize = 6;
        int valueFontSize = 8;
        int labelFontWeight = Font.BOLD;
        int valueFontWeight = Font.BOLD;

        PdfPTable pdfTable = new PdfPTable(1);
        pdfTable.setSpacingAfter( (float) 1.25 );

        PdfPCell cell = new PdfPCell(new Phrase(labelIn.trim(), getFont(labelFontSize, labelFontWeight)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);


        pdfTable.addCell( cell );

        cell = new PdfPCell(new Phrase(valueIn.trim(), getFont(valueFontSize, valueFontWeight)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
        cell.setUseDescender(true);
        cell.setPaddingTop( 0 );
        pdfTable.addCell( cell );

        return pdfTable;
    }

    private Image getImage(String imageName) throws IOException, BadElementException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("images/" + imageName);
        if (url != null) {
            return Image.getInstance(url);
        } else {
            throw new IOException("Error retrieving image: " + imageName);
        }
    }


    private PdfPTable createHeaderTable( Document documentIn,
                                         TransferItem transferItemIn ) throws DocumentException, BadElementException, MalformedURLException, IOException {
        int headerFontSize = 14;

        SimpleDateFormat dateFormatter = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss");

        String todaysDate = dateFormatter.format( new Date() );

        PdfPTable headerTable;

        headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);

        headerTable.setTotalWidth( documentIn.right() - documentIn.left() );
        headerTable.setSpacingAfter( 2 );

        headerTable.getDefaultCell().setPadding(5);

        String hdrImgName = "LetterOfTransmittalHdrLLCNyse.png";

        Image hdrImg = getImage( hdrImgName );

        PdfPCell cell = new PdfPCell( hdrImg, true );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseVariableBorders(true);
        cell.setBorderWidth( 0 );
        cell.setColspan(1);
//            cell.setBackgroundColor(new Color(0xDC, 0xDC, 0xDC));
//            cell.setUseDescender(true);
        headerTable.addCell(cell);

        return headerTable;
    }

    private Font getFont(String fontFamily, int fontSize, int weight, Color color) {
        Font font = FontFactory.getFont(fontFamily, fontSize, weight);
        if (color != null) {
            font.setColor(new BaseColor(color.getRGB()));
        }
        return font;
    }


    private Font getFont( String fontFamily, int fontSize, int weight ) {
        return FontFactory.getFont( fontFamily, fontSize, Font.NORMAL );
    }


    private Font getFont( int fontSize, int weight ) {
        return FontFactory.getFont( FontFactory.HELVETICA, fontSize, Font.NORMAL );
    }


    private Font getFont( int fontSize ) {
        return FontFactory.getFont(FontFactory.HELVETICA, fontSize, Font.NORMAL );
    }
}
