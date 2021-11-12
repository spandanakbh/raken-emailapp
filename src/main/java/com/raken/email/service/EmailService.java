package com.raken.email.service;

import com.raken.email.model.ElectronicMail;
import com.raken.email.model.EmailResponse;
import com.sendgrid.Response;

/**
 * @author spandana k
 *
 */
public interface EmailService {

	Response sendEmail(ElectronicMail email);

	EmailResponse validateInputAndGenerateResponseObject(ElectronicMail email);

}
